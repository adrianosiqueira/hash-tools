package hashtools.controller;

import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.algorithmsource.CheckBoxAlgorithmSource;
import hashtools.strategy.checksumgeneratorupdater.FileChecksumGeneratorUpdate;
import hashtools.strategy.checksumgeneratorupdater.TextChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputFileIdentification;
import hashtools.strategy.inputidentification.InputTextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
import hashtools.strategy.thread.VirtualThreadFactory;
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



    @FXML
    private void performChecksumGeneration() {
        threadFactory.newThread(() -> {
            // User feedback
            disableUi(pnlRoot);



            // Data retrieval
            ChecksumGenerationContext context = this.createGenerationContext();



            // Processing
            ChecksumGenerationContainer container = generationService.generateChecksums(context);
            container.consumeResultIfPresent(this::saveResult);
            container.consumeProblemIfPresent(this::showMessageDialog);
            container.consumeExceptionIfPresent(this::logException);
        }).start();
    }

    @FXML
    private void openInputFile() {
        super.openFile(
            "Select the input file",
            FileDialog::openForReading,
            file -> txtInput.setText(file.toString())
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

    private void saveResult(ChecksumGenerationResult result) {
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

    private void showMessageDialog(String message) {
        super.showMessageDialog("Hash Tools", "Problem", message);
        super.enableUi(pnlRoot);
    }



    @Override
    protected void logException(Exception exception) {
        super.logException(exception);
        super.enableUi(pnlRoot);
    }
}
