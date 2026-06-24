package hashtools.module.comparator.controller;

import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileDialog;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.comparator.domain.ChecksumComparisonContainer;
import hashtools.module.comparator.domain.ChecksumComparisonParameter;
import hashtools.module.comparator.domain.ChecksumComparisonResult;
import hashtools.module.comparator.service.ComparatorService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ComparatorController implements Initializable {

    @FXML
    private TextField txtInput1;
    @FXML
    private CheckBox chkInput1;

    @FXML
    private TextField txtInput2;
    @FXML
    private CheckBox chkInput2;

    @FXML
    private Button btnExecute;
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
            disableUi();



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
        EnhancedFile file = new FileDialog()
            .withTitle("Select the first input file")
            .openForReading()
            .orElse(null);

        if (file == null) {
            return;
        }

        txtInput1.setText(file.toString());
    }

    @FXML
    private void openInputFile2() {
        EnhancedFile file = new FileDialog()
            .withTitle("Select the second input file")
            .openForReading()
            .orElse(null);

        if (file == null) {
            return;
        }

        txtInput2.setText(file.toString());
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
        prgEquality.setProgress(progress);
    }

    private void presentResult(ChecksumComparisonResult result) {
        double equality = result.calculateEquality();
        prgEquality.setProgress(equality);
        enableUi();

        IO.println(result.getChecksum());
        IO.println("Equality: " + (equality * 100) + "%");
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
        prgEquality.setProgress(0.0);
    }

    private void enableUi() {
        btnExecute.setDisable(false);
    }
}
