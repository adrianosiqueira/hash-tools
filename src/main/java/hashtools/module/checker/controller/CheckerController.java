package hashtools.module.checker.controller;

import hashtools.core.source.ChecksumSource;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checker.domain.ChecksumCheckingCallback;
import hashtools.module.checker.domain.ChecksumCheckingParameter;
import hashtools.module.checker.domain.ChecksumCheckingResult;
import hashtools.module.checker.service.CheckerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class CheckerController implements Initializable {

    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;

    @FXML
    private TextField txtChecksum;
    @FXML
    private CheckBox chkChecksum;

    @FXML
    private Button btnExecute;
    @FXML
    private ProgressBar prgReliability;

    private CheckerService checkerService;
    private ExecutorService localThreadPool;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkerService = new CheckerService();
        this.localThreadPool = ThreadPoolFactory.cachedDaemonPool();
    }



    @FXML
    private void performChecksumChecking() {
        localThreadPool.execute(() -> {
            // User feedback
            disableUi();



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            ChecksumSource checksumSource = this.createChecksumSource();



            // Communication setup
            ChecksumCheckingParameter parameter = new ChecksumCheckingParameter();
            parameter.setInputSource(inputSource);
            parameter.setChecksumSource(checksumSource);

            ChecksumCheckingCallback callback = new ChecksumCheckingCallback();
            callback.addProgressConsumer(this::trackProgress);
            callback.addResultConsumer(this::presentResult);
            callback.addProblemConsumer(this::showMessageDialog);
            callback.addExceptionConsumer(this::logException);



            // Processing
            checkerService.performChecksumChecking(
                parameter,
                callback
            );
        });
    }

    @FXML
    private void openInputFile() {
    }

    @FXML
    private void openChecksumFile() {
    }



    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? InputSource.fileInputSource(txtInput.getText())
            : InputSource.textInputSource(txtInput.getText());
    }

    private ChecksumSource createChecksumSource() {
        return chkChecksum.isSelected()
            ? ChecksumSource.fileChecksumSource(txtChecksum.getText())
            : ChecksumSource.textChecksumSource(txtChecksum.getText());
    }



    private void trackProgress(double progress) {
        prgReliability.setProgress(progress);
    }

    private void presentResult(ChecksumCheckingResult result) {
        double reliability = result.calculateReliability();
        prgReliability.setProgress(reliability);
        enableUi();

        result.getChecksums().forEach(IO::println);
        IO.println("Reliability: " + (reliability * 100) + "%");
    }

    private void showMessageDialog(String message) {
        IO.println(message);
        enableUi();
    }

    private void logException(Exception exception) {
        //noinspection CallToPrintStackTrace
        exception.printStackTrace();
        enableUi();
    }



    private void disableUi() {
        btnExecute.setDisable(true);
    }

    private void enableUi() {
        btnExecute.setDisable(false);
    }
}
