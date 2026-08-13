package hashtools.controller;

import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.algorithmsource.CheckBoxAlgorithmSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
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
            // User feedback
            super.disableUi(pnlRoot);



            // Data retrieval
            ChecksumGenerationContext context = this.createGenerationContext();



            // Processing
            switch (generationService.generateChecksums(context)) {
                case CanceledResult _ -> {}
                case ChecksumGenerationResult result -> this.processResult(result);
                case ExceptionResult result -> this.processResult(result);
                case ProblemResult result -> this.processResult(result);
            }



            // User feedback
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



    private ChecksumGenerationContext createGenerationContext() {
        InputSource inputSource = chkInput.isSelected()
            ? new FileInputSource(txtInput.getText())
            : new TextInputSource(txtInput.getText());

        inputSource.setProgressTracking(this::trackProgress);



        ChecksumGenerationContext context = new ChecksumGenerationContext();
        context.setAlgorithmSource(new CheckBoxAlgorithmSource(pnlAlgorithm));
        context.setInputSource(inputSource);

        return context;
    }

    private void processResult(ExceptionResult result) {
        result.consumeException(super::logException);
    }

    private void processResult(ProblemResult result) {
        super.showMessageDialog("Hash Tools", "Problem", result.getDescription());
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
