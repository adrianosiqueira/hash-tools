package hashtools.module.checker.controller;

import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileDialog;
import hashtools.core.file.FileExtension;
import hashtools.core.source.ChecksumSource;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.checker.domain.ChecksumCheckingContainer;
import hashtools.module.checker.domain.ChecksumCheckingParameter;
import hashtools.module.checker.domain.ChecksumCheckingResult;
import hashtools.module.checker.service.CheckerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class CheckerController implements Initializable {

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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkerService = new CheckerService();
    }



    @FXML
    private void performChecksumChecking() {
        ThreadPool.CACHED_DAEMON.execute(() -> {
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
            ? InputSource.fileInputSource(txtInput.getText())
            : InputSource.textInputSource(txtInput.getText());
    }

    private ChecksumSource createChecksumSource() {
        return chkChecksum.isSelected()
            ? ChecksumSource.fileChecksumSource(txtChecksum.getText())
            : ChecksumSource.textChecksumSource(txtChecksum.getText());
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
