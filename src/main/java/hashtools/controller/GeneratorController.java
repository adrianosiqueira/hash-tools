package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.generatorupdate.FileGeneratorUpdate;
import hashtools.strategy.generatorupdate.TextGeneratorUpdate;
import hashtools.strategy.identification.FileIdentification;
import hashtools.strategy.identification.TextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
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

    private ChecksumGenerationService generationService;
    private ThreadFactory threadFactory;
    private EnhancedFile tempFile;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generationService = new ChecksumGenerationService();
        this.threadFactory = new VirtualThreadFactory();
        this.tempFile = EnhancedFile.createTemporaryFile();
    }

    @Override
    public void stopAllServicesProcessing() {
        generationService.cancelChecksumGeneration();

        try {
            tempFile.delete();
        } catch (Exception e) {
            super.logException(e);
        }
    }



    @FXML
    private void performChecksumGeneration() {
        threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();
            this.hideResultActions();

            this.setupTheService();
            generationService.generateChecksums();

            super.enableUi(pnlRoot);
            this.showResultActions();
        }).start();
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
        Label contentNode = new Label();
        contentNode.setWrapText(false);

        try {
            contentNode.setText(tempFile.getContent());
        } catch (Exception e) {
            super.logException(e);
            contentNode.setText("Error: " + e.getMessage());
        }

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Hash Tools");
        dialog.setHeaderText("Generated checksums");
        dialog.setResizable(true);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(contentNode);

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

    private void setupTheService() {
        generationService.initSetup();

        generationService.setExceptionConsumer(super::logException);
        generationService.setProblemConsumer(this::reportProblem);
        generationService.setProgressConsumer(this::trackProgress);
        generationService.setResultConsumer(this::processResult);



        if (chkInput.isSelected()) {
            generationService.setInputProblemDetection(new InputFileProblemDetection(txtInput.getText()));
            generationService.setGeneratorUpdate(new FileGeneratorUpdate(txtInput.getText()));
            generationService.setIdentification(new FileIdentification(txtInput.getText()));
        } else {
            generationService.setInputProblemDetection(new InputTextProblemDetection(txtInput.getText()));
            generationService.setGeneratorUpdate(new TextGeneratorUpdate(txtInput.getText()));
            generationService.setIdentification(new TextIdentification(txtInput.getText()));
        }



        Collection<Algorithm> algorithms = pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .filter(CheckBox::isSelected)
            .map(CheckBox::getText)
            .map(Algorithm::getByName)
            .flatMap(Optional::stream)
            .toList();

        generationService.setAlgorithms(algorithms);
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
