package hashtools.controller;

import hashtools.domain.checksum.Algorithm;
import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileDialog;
import hashtools.domain.parameter.ChecksumGenerationParameter;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.generatorupdate.FileGeneratorUpdate;
import hashtools.strategy.generatorupdate.TextGeneratorUpdate;
import hashtools.strategy.identification.FileIdentification;
import hashtools.strategy.identification.TextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import hashtools.window.MessageDialog;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadFactory;

public class GeneratorController extends AbstractController {

    @FXML
    private Pane pnlRoot;

    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;

    @FXML
    private Pane pnlAlgorithm;

    @FXML
    private ProgressBar prgProgress;

    @FXML
    private Pane pnlResult;

    private ThreadFactory threadFactory;
    private Thread checksumGenerationThread;
    private EnhancedFile tempFile;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.threadFactory = new VirtualThreadFactory();
        this.checksumGenerationThread = new Thread(() -> {});
        this.tempFile = EnhancedFile.createTemporaryFile();
    }

    @Override
    public void stopAllServicesProcessing() {
        try {
            checksumGenerationThread.interrupt();
            tempFile.delete();
        } catch (Exception e) {
            super.logException(e);
        }
    }



    @FXML
    private void performChecksumGeneration() {
        this.checksumGenerationThread = threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();
            this.hideResultActions();



            var parameter = this.createChecksumGenerationParameter();

            var service = new ChecksumGenerationService();
            var generationResult = service.generateChecksums(parameter);

            if (generationResult.isOk()) {
                var result = generationResult.getValue();
                this.processResult(result);
            } else {
                var error = generationResult.getError();
                this.reportProblem(error);
            }



            super.enableUi(pnlRoot);
            this.showResultActions();
        });
        checksumGenerationThread.start();
    }

    @FXML
    private void openInputFile() {
        super.openFile(
            "Select the input file",
            FileDialog::openForReading,
            file -> txtInput.setText(file.getAbsolutePath())
        );
    }

    @FXML
    private void selectAllAlgorithms() {
        pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .forEach(checkBox -> checkBox.setSelected(true));
    }

    @FXML
    private void selectNoAlgorithms() {
        pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .forEach(checkBox -> checkBox.setSelected(false));
    }

    @FXML
    private void invertAlgorithmsSelection() {
        pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .forEach(checkBox -> checkBox.setSelected(!checkBox.isSelected()));
    }

    @FXML
    private void saveResultToFile() {
        super.openFile(
            "Select where to save the checksums",
            FileDialog::openForWriting,
            file -> {
                try {
                    tempFile.copyTo(file);
                } catch (Exception e) {
                    super.logException(e);
                }
            }
        );
    }

    @FXML
    private void showResultIntoDialog() {
        MessageDialog dialog = new MessageDialog();
        dialog.setTitle("Hash Tools");
        dialog.setHeader("Generated checksums");

        try {
            dialog.setMessage(tempFile.getContent());
        } catch (Exception e) {
            super.logException(e);
            dialog.setMessage("Error: " + e.getMessage());
        }

        dialog.show();
    }



    private void hideResultActions() {
        pnlResult
            .getChildren()
            .forEach(node -> node.setVisible(false));
    }

    private void showResultActions() {
        pnlResult
            .getChildren()
            .forEach(node -> node.setVisible(true));
    }

    private ChecksumGenerationParameter createChecksumGenerationParameter() {
        var algorithms = pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .filter(CheckBox::isSelected)
            .map(CheckBox::getText)
            .map(Algorithm::getByName)
            .flatMap(Optional::stream)
            .toList();

        var parameter = new ChecksumGenerationParameter();
        parameter.setAlgorithms(algorithms);
        parameter.setProgressTracker(this::trackProgress);

        if (chkInput.isSelected()) {
            parameter.setInputProblemDetection(new InputFileProblemDetection(txtInput.getText()));
            parameter.setGeneratorUpdate(new FileGeneratorUpdate(txtInput.getText()));
            parameter.setIdentification(new FileIdentification(txtInput.getText()));
        } else {
            parameter.setInputProblemDetection(new InputTextProblemDetection(txtInput.getText()));
            parameter.setGeneratorUpdate(new TextGeneratorUpdate(txtInput.getText()));
            parameter.setIdentification(new TextIdentification(txtInput.getText()));
        }

        return parameter;
    }

    private void reportProblem(String problem) {
        super.showMessageDialog("Hash Tools", "Problem", problem);
    }

    private void processResult(ChecksumGenerationResult result) {
        try {
            String content = result.formatForSaving();
            tempFile.replaceContent(content);
        } catch (IOException e) {
            super.logException(e);
        }
    }

    @Override
    protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }
}
