package hashtools.controller;

import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.GeneratorService;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.algorithmsource.CheckBoxAlgorithmSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
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

    private GeneratorService generatorService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generatorService = new GeneratorService();
        this.threadFactory = new VirtualThreadFactory();
    }



    @FXML
    private void performChecksumGeneration() {
        threadFactory.newThread(() -> {
            // User feedback
            disableUi(pnlRoot);



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            AlgorithmSource algorithmSource = this.createAlgorithmSource();



            // Communication setup
            ChecksumGenerationContext parameter = new ChecksumGenerationContext();
            parameter.setInputSource(inputSource);
            parameter.setAlgorithmSource(algorithmSource);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumGenerationContainer container = generatorService.performChecksumGeneration(parameter);
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



    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? new FileInputSource(txtInput.getText())
            : new TextInputSource(txtInput.getText());
    }

    private AlgorithmSource createAlgorithmSource() {
        return new CheckBoxAlgorithmSource(pnlAlgorithm);
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
    protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }

    @Override
    protected void logException(Exception exception) {
        super.logException(exception);
        super.enableUi(pnlRoot);
    }
}
