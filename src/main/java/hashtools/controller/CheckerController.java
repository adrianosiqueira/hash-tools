package hashtools.controller;

import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.service.ChecksumCheckingService;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.checksumsource.FileChecksumSource;
import hashtools.strategy.checksumsource.TextChecksumSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
    private Label lblReliability;

    private ChecksumCheckingService checksumCheckingService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checksumCheckingService = new ChecksumCheckingService();
        this.threadFactory = new VirtualThreadFactory();
    }

    @Override
    public void stopAllServicesProcessing() {
        checksumCheckingService.cancelChecksumChecking();
    }



    @FXML
    private void performChecksumChecking() {
        threadFactory.newThread(() -> {
            // User feedback
            super.disableUi(pnlRoot);
            this.cleanUi();



            // Data retrieval
            ChecksumCheckingContext context = this.createCheckingContext();



            // Processing
            switch (checksumCheckingService.checkChecksums(context)) {
                case CanceledResult _ -> {}
                case ExceptionResult result -> this.processResult(result);
                case ProblemResult result -> this.processResult(result);
                case ChecksumCheckingResult result -> this.processResult(result);
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
    private void openChecksumFile() {
        super.openFile(
            "Select the checksum file",
            FileDialog::openForReading,
            file -> txtChecksum.setText(file.getAbsolutePath())
        );
    }



    private ChecksumCheckingContext createCheckingContext() {
        InputSource inputSource = chkInput.isSelected()
            ? new FileInputSource(txtInput.getText())
            : new TextInputSource(txtInput.getText());

        ChecksumSource checksumSource = chkChecksum.isSelected()
            ? new FileChecksumSource(txtChecksum.getText())
            : new TextChecksumSource(txtChecksum.getText());

        inputSource.setProgressTracking(this::trackProgress);



        ChecksumCheckingContext context = new ChecksumCheckingContext();
        context.setInputSource(inputSource);
        context.setChecksumSource(checksumSource);

        return context;
    }

    private void processResult(ExceptionResult result) {
        result.consumeException(super::logException);
    }

    private void processResult(ProblemResult result) {
        super.showMessageDialog("Hash Tools", "Problem", result.getDescription());
    }

    private void processResult(ChecksumCheckingResult result) {
        double reliability = result.calculateReliability();
        Platform.runLater(() -> lblReliability.setText("%.0f %%".formatted(reliability)));
    }



    @Override
    protected void cleanUi() {
        Platform.runLater(() -> lblReliability.setText(""));
    }

    @Override
    protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }
}
