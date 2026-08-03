package hashtools.controller;

import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.algorithmsource.CheckBoxAlgorithmSource;
import hashtools.strategy.checksumgeneratorupdater.FileChecksumGeneratorUpdate;
import hashtools.strategy.checksumgeneratorupdater.TextChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputFileIdentification;
import hashtools.strategy.inputidentification.InputTextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
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
    public void close() {
        generationService.cancelChecksumGeneration();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generationService = new ChecksumGenerationService();
        this.threadFactory = new VirtualThreadFactory();
    }



    @FXML
    private void performChecksumGeneration() {
        threadFactory.newThread(() -> {
            // User feedback
            disableUi(pnlRoot);



            // Data retrieval
            ChecksumGenerationContext context = this.createGenerationContext();



            // Processing
            switch (generationService.generateChecksums(context)) {
                case CanceledResult _ -> {}
                case ChecksumGenerationResult result -> this.processResult(result);
                case ExceptionResult result -> this.processResult(result);
                case ProblemResult result -> this.processResult(result);
            }
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



    private ChecksumGenerationContext createGenerationContext() {
        ChecksumGenerationContext context = new ChecksumGenerationContext();
        context.setAlgorithmSource(new CheckBoxAlgorithmSource(pnlAlgorithm));

        if (chkInput.isSelected()) {
            context.setInputIdentification(new InputFileIdentification(txtInput.getText()));
            context.setChecksumGeneratorUpdate(new FileChecksumGeneratorUpdate(txtInput.getText()));
            context.setProblemDetection(new InputFileProblemDetection(txtInput.getText()));
        } else {
            context.setInputIdentification(new InputTextIdentification(txtInput.getText()));
            context.setChecksumGeneratorUpdate(new TextChecksumGeneratorUpdate(txtInput.getText()));
            context.setProblemDetection(new InputTextProblemDetection(txtInput.getText()));
        }

        return context;
    }

    private void processResult(ExceptionResult result) {
        result.consumeException(super::logException);
        super.enableUi(pnlRoot);
    }

    private void processResult(ProblemResult result) {
        super.showMessageDialog("Hash Tools", "Problem", result.getDescription());
        super.enableUi(pnlRoot);
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

        super.enableUi(pnlRoot);
    }
}
