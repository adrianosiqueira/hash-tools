package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.generatorupdate.FileGeneratorUpdate;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.generatorupdate.TextGeneratorUpdate;
import hashtools.strategy.identification.FileIdentification;
import hashtools.strategy.identification.Identification;
import hashtools.strategy.identification.TextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
import hashtools.strategy.problemdetection.ProblemDetection;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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

    private ChecksumGenerationService generationService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generationService = new ChecksumGenerationService();
        this.threadFactory = new VirtualThreadFactory();
    }

    @Override
    public void stopAllServicesProcessing() {
        generationService.cancelChecksumGeneration();
    }



    @FXML
    private void performChecksumGeneration() {
        threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);

            this.setupTheService();
            generationService.generateChecksums();

            super.enableUi(pnlRoot);
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



    private void setupTheService() {
        generationService.initSetup();

        generationService.setExceptionConsumer(super::logException);
        generationService.setProblemConsumer(this::reportProblem);
        generationService.setProgressConsumer(this::trackProgress);
        generationService.setResultConsumer(this::processResult);



        ProblemDetection inputProblemDetection = chkInput.isSelected()
            ? new InputFileProblemDetection(txtInput.getText())
            : new InputTextProblemDetection(txtInput.getText());

        generationService.setInputProblemDetection(inputProblemDetection);



        GeneratorUpdate generatorUpdate = chkInput.isSelected()
            ? new FileGeneratorUpdate(txtInput.getText())
            : new TextGeneratorUpdate(txtInput.getText());

        generationService.setGeneratorUpdate(generatorUpdate);



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



        Identification identification = chkInput.isSelected()
            ? new FileIdentification(txtInput.getText())
            : new TextIdentification(txtInput.getText());

        generationService.setIdentification(identification);
    }

    private void reportProblem(String problem) {
        super.showMessageDialog("Hash Tools", "Problem", problem);
    }

    private void processResult(ChecksumGenerationResult result) {
        super.openFile(
            "Select where to save the checksums",
            FileDialog::openForWriting,
            file -> {
                try {
                    String content = result.formatForSaving();
                    file.replaceContent(content);
                } catch (IOException e) {
                    super.logException(e);
                }
            }
        );
    }

    @Override
    protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }
}
