package hashtools.controller;

import hashtools.domain.container.ChecksumCheckingContainer;
import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.service.CheckerService;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.checksumsource.FileChecksumSource;
import hashtools.strategy.checksumsource.TextChecksumSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
import hashtools.strategy.thread.VirtualThreadFactory;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadFactory;

public class CheckerController extends AbstractController {

    @FXML
    private Pane pnlRoot;

    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;

    @FXML
    private TextField txtChecksum;
    @FXML
    private CheckBox chkChecksum;

    @FXML
    private ProgressBar prgProgress;
    @FXML
    private ProgressBar prgReliability;

    private CheckerService checkerService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkerService = new CheckerService();
        this.threadFactory = new VirtualThreadFactory();
    }



    @FXML
    private void performChecksumChecking() {
        threadFactory.newThread(() -> {
            // User feedback
            super.disableUi(pnlRoot);
            super.cleanUi();



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            ChecksumSource checksumSource = this.createChecksumSource();



            // Communication setup
            ChecksumCheckingContext parameter = new ChecksumCheckingContext();
            parameter.setInputSource(inputSource);
            parameter.setChecksumSource(checksumSource);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumCheckingContainer container = checkerService.performChecksumChecking(parameter);
            container.consumeResultIfPresent(this::presentResult);
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

    @FXML
    private void openChecksumFile() {
        super.openFile(
            "Select the checksum file",
            FileDialog::openForReading,
            file -> txtChecksum.setText(file.toString())
        );
    }



    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? new FileInputSource(txtInput.getText())
            : new TextInputSource(txtInput.getText());
    }

    private ChecksumSource createChecksumSource() {
        return chkChecksum.isSelected()
            ? new FileChecksumSource(txtChecksum.getText())
            : new TextChecksumSource(txtChecksum.getText());
    }

    private void presentResult(ChecksumCheckingResult result) {
        double reliability = result.calculateReliability();
        prgReliability.setProgress(reliability);
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
        enableUi(pnlRoot);
    }

    @Override
    protected void cleanUi() {
        prgReliability.setProgress(0.0);
    }
}
