package hashtools.module.comparator.controller;

import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileDialog;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.comparator.domain.ChecksumComparisonContainer;
import hashtools.module.comparator.domain.ChecksumComparisonParameter;
import hashtools.module.comparator.domain.ChecksumComparisonResult;
import hashtools.module.comparator.service.ComparatorService;
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

public class ComparatorController implements Initializable {

    @FXML
    private Pane pnlRoot;

    @FXML
    private TextField txtInput1;
    @FXML
    private CheckBox chkInput1;

    @FXML
    private TextField txtInput2;
    @FXML
    private CheckBox chkInput2;

    @FXML
    private ProgressBar prgEquality;

    private ComparatorService comparatorService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.comparatorService = new ComparatorService();
    }



    @FXML
    private void performChecksumComparison() {
        ThreadPool.CACHED_DAEMON.execute(() -> {
            // User feedback
            disableUi(pnlRoot);



            // Data retrieval
            InputSource inputSource1 = this.createInputSource1();
            InputSource inputSource2 = this.createInputSource2();



            // Communication setup
            ChecksumComparisonParameter parameter = new ChecksumComparisonParameter();
            parameter.setInputSource1(inputSource1);
            parameter.setInputSource2(inputSource2);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumComparisonContainer container = comparatorService.performChecksumComparison(parameter);
            container.consumeResultIfPresent(this::presentResult);
            container.consumeProblemIfPresent(this::showMessageDialog);
            container.consumeExceptionIfPresent(this::logException);
        });
    }

    @FXML
    private void openInputFile1() {
        new FileDialog()
            .withTitle("Select the first input file")
            .openForReading()
            .map(EnhancedFile::toString)
            .ifPresent(txtInput1::setText);
    }

    @FXML
    private void openInputFile2() {
        new FileDialog()
            .withTitle("Select the second input file")
            .openForReading()
            .map(EnhancedFile::toString)
            .ifPresent(txtInput2::setText);
    }



    private InputSource createInputSource1() {
        return chkInput1.isSelected()
            ? InputSource.fileInputSource(txtInput1.getText())
            : InputSource.textInputSource(txtInput1.getText());
    }

    private InputSource createInputSource2() {
        return chkInput2.isSelected()
            ? InputSource.fileInputSource(txtInput2.getText())
            : InputSource.textInputSource(txtInput2.getText());
    }



    private void trackProgress(double progress) {
        Platform.runLater(() -> prgEquality.setProgress(progress));
    }

    private void presentResult(ChecksumComparisonResult result) {
        double equality = result.calculateEquality();
        prgEquality.setProgress(equality);
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
}
