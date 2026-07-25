package hashtools.controller;

import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.file.FileDialog;
import hashtools.service.ChecksumCheckingService;
import hashtools.strategy.checksumextraction.FileChecksumExtraction;
import hashtools.strategy.checksumextraction.TextChecksumExtraction;
import hashtools.strategy.checksumgeneratorupdater.FileChecksumGeneratorUpdate;
import hashtools.strategy.checksumgeneratorupdater.TextChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputFileIdentification;
import hashtools.strategy.inputidentification.InputTextIdentification;
import hashtools.strategy.problemdetection.ChecksumFileProblemDetection;
import hashtools.strategy.problemdetection.ChecksumTextProblemDetection;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
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

    private ChecksumCheckingService checksumCheckingService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checksumCheckingService = new ChecksumCheckingService();
        this.threadFactory = new VirtualThreadFactory();
    }



    @FXML
    private void performChecksumChecking() {
        threadFactory.newThread(() -> {
            // User feedback
            super.disableUi(pnlRoot);
            super.cleanUi();



            // Data retrieval
            ChecksumCheckingContext context = this.createCheckingContext();



            // Processing
            switch (checksumCheckingService.checkChecksums(context)) {
                case ChecksumCheckingService.Result.Exception exception -> this.processResult(exception);
                case ChecksumCheckingService.Result.Problem problem -> this.processResult(problem);
                case ChecksumCheckingService.Result.Success success -> this.processResult(success);
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

    @FXML
    private void openChecksumFile() {
        super.openFile(
            "Select the checksum file",
            FileDialog::openForReading,
            file -> txtChecksum.setText(file.getAbsolutePath())
        );
    }



    private ChecksumCheckingContext createCheckingContext() {
        ChecksumCheckingContext context = new ChecksumCheckingContext();

        if (chkInput.isSelected()) {
            context.setInputIdentification(new InputFileIdentification(txtInput.getText()));
            context.setChecksumGeneratorUpdate(new FileChecksumGeneratorUpdate(txtInput.getText()));
            context.setInputProblemDetection(new InputFileProblemDetection(txtInput.getText()));
        } else {
            context.setInputIdentification(new InputTextIdentification(txtInput.getText()));
            context.setChecksumGeneratorUpdate(new TextChecksumGeneratorUpdate(txtInput.getText()));
            context.setInputProblemDetection(new InputTextProblemDetection(txtInput.getText()));
        }

        if (chkChecksum.isSelected()) {
            context.setChecksumProblemDetection(new ChecksumFileProblemDetection(txtChecksum.getText()));
            context.setChecksumExtraction(new FileChecksumExtraction(txtChecksum.getText()));
        } else {
            context.setChecksumProblemDetection(new ChecksumTextProblemDetection(txtChecksum.getText()));
            context.setChecksumExtraction(new TextChecksumExtraction(txtChecksum.getText()));
        }

        return context;
    }

    private void processResult(ChecksumCheckingService.Result.Exception result) {
        super.logException(result.throwable());
        super.enableUi(pnlRoot);
    }

    private void processResult(ChecksumCheckingService.Result.Problem result) {
        super.showMessageDialog("Hash Tools", "Problem", result.problem());
        super.enableUi(pnlRoot);
    }

    private void processResult(ChecksumCheckingService.Result.Success result) {
        double reliability = result
            .result()
            .calculateReliability();

        prgReliability.setProgress(reliability);
        super.enableUi(pnlRoot);
    }



    @Override
    protected void cleanUi() {
        prgReliability.setProgress(0.0);
    }
}
