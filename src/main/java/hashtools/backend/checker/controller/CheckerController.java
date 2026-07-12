package hashtools.backend.checker.controller;

import hashtools.backend.checker.domain.ChecksumCheckingContainer;
import hashtools.backend.checker.domain.ChecksumCheckingParameter;
import hashtools.backend.checker.domain.ChecksumCheckingResult;
import hashtools.backend.checker.service.CheckerService;
import hashtools.backend.core.file.EnhancedFile;
import hashtools.backend.core.file.FileDialog;
import hashtools.backend.core.file.FileExtension;
import hashtools.backend.core.interfaces.ChecksumSource;
import hashtools.backend.core.interfaces.Controller;
import hashtools.backend.core.interfaces.InputSource;
import hashtools.backend.core.interfaces.ThreadPool;
import hashtools.backend.core.strategy.checksumsource.FileChecksumSource;
import hashtools.backend.core.strategy.checksumsource.TextChecksumSource;
import hashtools.backend.core.strategy.inputsource.FileInputSource;
import hashtools.backend.core.strategy.inputsource.TextInputSource;
import hashtools.backend.core.strategy.threadpool.UnlimitedCoreDaemonThreadPool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckerController implements Controller {

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
    private ThreadPool lightTaskThreadPool;



    @Override
    public void close() {
        lightTaskThreadPool.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkerService = new CheckerService();
        this.lightTaskThreadPool = new UnlimitedCoreDaemonThreadPool();
    }



    @FXML
    private void performChecksumChecking() {
        lightTaskThreadPool.run(() -> {
            // User feedback
            disableUi(pnlRoot);
            cleanUi();



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            ChecksumSource checksumSource = this.createChecksumSource();



            // Communication setup
            ChecksumCheckingParameter parameter = new ChecksumCheckingParameter();
            parameter.setInputSource(inputSource);
            parameter.setChecksumSource(checksumSource);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumCheckingContainer container = checkerService.performChecksumChecking(parameter);
            container.consumeResultIfPresent(this::presentResult);
            container.consumeProblemIfPresent(this::showMessageDialog);
            container.consumeExceptionIfPresent(this::logException);
        });
    }

    @FXML
    private void openInputFile() {
        new FileDialog()
            .withTitle("Select the input file")
            .openForReading()
            .map(EnhancedFile::toString)
            .ifPresent(txtInput::setText);
    }

    @FXML
    private void openChecksumFile() {
        new FileDialog()
            .withTitle("Select the checksum file")
            .withDefaultExtension(FileExtension.HASH)
            .openForReading()
            .map(EnhancedFile::toString)
            .ifPresent(txtChecksum::setText);
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



    private void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }

    private void presentResult(ChecksumCheckingResult result) {
        double reliability = result.calculateReliability();
        prgReliability.setProgress(reliability);
        enableUi(pnlRoot);
    }

    private void showMessageDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hash Tools");
            alert.setHeaderText("Problem");
            alert.setContentText(message);
            alert.show();
        });

        enableUi(pnlRoot);
    }

    private void logException(Exception exception) {
        //noinspection CallToPrintStackTrace
        exception.printStackTrace();
        enableUi(pnlRoot);
    }



    private void disableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.WAIT);
            pane.getChildren().forEach(this::disableUi);
        } else if (node instanceof Control control) {
            control.setDisable(true);
        }
    }

    private void enableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.DEFAULT);
            pane.getChildren().forEach(this::enableUi);
        } else if (node instanceof Control control) {
            control.setDisable(false);
        }
    }

    private void cleanUi() {
        prgReliability.setProgress(0.0);
    }
}
