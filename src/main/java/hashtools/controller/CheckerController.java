package hashtools.controller;

import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.service.ChecksumCheckingService;
import hashtools.strategy.checksumextraction.FileChecksumExtraction;
import hashtools.strategy.checksumextraction.TextChecksumExtraction;
import hashtools.strategy.generatorupdate.FileGeneratorUpdate;
import hashtools.strategy.generatorupdate.TextGeneratorUpdate;
import hashtools.strategy.problemdetection.ChecksumFileProblemDetection;
import hashtools.strategy.problemdetection.ChecksumTextProblemDetection;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
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

    private ChecksumCheckingService checkingService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkingService = new ChecksumCheckingService();
        this.threadFactory = new VirtualThreadFactory();
    }

    @Override
    public void stopAllServicesProcessing() {
        checkingService.cancelChecksumChecking();
    }



    @FXML
    private void performChecksumChecking() {
        threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();

            this.setupService();
            checkingService.checkChecksums();

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



    private void setupService() {
        checkingService.initSetup();
        checkingService.setExceptionConsumer(super::logException);
        checkingService.setProblemConsumer(this::reportProblem);
        checkingService.setProgressConsumer(this::trackProgress);
        checkingService.setResultConsumer(this::processResult);



        if (chkInput.isSelected()) {
            checkingService.setInputProblemDetection(new InputFileProblemDetection(txtInput.getText()));
            checkingService.setGeneratorUpdate(new FileGeneratorUpdate(txtInput.getText()));
        } else {
            checkingService.setInputProblemDetection(new InputTextProblemDetection(txtInput.getText()));
            checkingService.setGeneratorUpdate(new TextGeneratorUpdate(txtInput.getText()));
        }



        if (chkChecksum.isSelected()) {
            checkingService.setChecksumProblemDetection(new ChecksumFileProblemDetection(txtChecksum.getText()));
            checkingService.setChecksumExtraction(new FileChecksumExtraction(txtChecksum.getText()));
        } else {
            checkingService.setChecksumProblemDetection(new ChecksumTextProblemDetection(txtChecksum.getText()));
            checkingService.setChecksumExtraction(new TextChecksumExtraction(txtChecksum.getText()));
        }
    }

    private void reportProblem(String problem) {
        super.showMessageDialog("Hash Tools", "Problem", problem);
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
