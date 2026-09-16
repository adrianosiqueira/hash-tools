package hashtools.controller;

import hashtools.domain.file.FileDialog;
import hashtools.domain.parameter.ChecksumCheckingParameter;
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
import hashtools.strategy.threadfactory.ThreadFactories;
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

    private ThreadFactory threadFactory;
    private Thread checksumCheckingThread;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.threadFactory = ThreadFactories::newVirtual;
        this.checksumCheckingThread = new Thread(() -> {});
    }

    @Override
    public void stopAllServicesProcessing() {
        checksumCheckingThread.interrupt();
    }



    @FXML
    private void performChecksumChecking() {
        this.checksumCheckingThread = threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();



            var parameter = this.createChecksumCheckingParameter();
            var service = new ChecksumCheckingService();

            var checkingResult = service.checkChecksums(parameter);

            if (checkingResult.isOk()) {
                var result = checkingResult.getValue();
                this.processResult(result);
            } else {
                var error = checkingResult.getError();
                this.reportProblem(error);
            }



            super.enableUi(pnlRoot);
        });
        checksumCheckingThread.start();
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



    private ChecksumCheckingParameter createChecksumCheckingParameter() {
        var parameter = new ChecksumCheckingParameter();
        parameter.setProgressTracker(this::trackProgress);

        if (chkInput.isSelected()) {
            parameter.setInputProblemDetection(new InputFileProblemDetection(txtInput.getText()));
            parameter.setGeneratorUpdate(new FileGeneratorUpdate(txtInput.getText()));
        } else {
            parameter.setInputProblemDetection(new InputTextProblemDetection(txtInput.getText()));
            parameter.setGeneratorUpdate(new TextGeneratorUpdate(txtInput.getText()));
        }

        if (chkChecksum.isSelected()) {
            parameter.setChecksumProblemDetection(new ChecksumFileProblemDetection(txtChecksum.getText()));
            parameter.setChecksumExtraction(new FileChecksumExtraction(txtChecksum.getText()));
        } else {
            parameter.setChecksumProblemDetection(new ChecksumTextProblemDetection(txtChecksum.getText()));
            parameter.setChecksumExtraction(new TextChecksumExtraction(txtChecksum.getText()));
        }

        return parameter;
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
