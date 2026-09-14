package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.file.FileDialog;
import hashtools.domain.parameter.ChecksumComparisonParameter;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.service.ChecksumComparisonService;
import hashtools.strategy.generatorupdate.FileGeneratorUpdate;
import hashtools.strategy.generatorupdate.TextGeneratorUpdate;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
import hashtools.strategy.threadfactory.VirtualThreadFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadFactory;

public class ComparatorController extends AbstractController {

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
    private ComboBox<Algorithm> cmbAlgorithm;

    @FXML
    private ProgressBar prgProgress;
    @FXML
    private Label lblEquality;

    private ThreadFactory threadFactory;
    private Thread checksumComparisonThread;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.threadFactory = new VirtualThreadFactory();
        this.checksumComparisonThread = new Thread(() -> {});
        this.setupAlgorithms();
    }

    @Override
    public void stopAllServicesProcessing() {
        checksumComparisonThread.interrupt();
    }



    @FXML
    private void performChecksumComparison() {
        this.checksumComparisonThread = threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();

            var parameter = this.createChecksumComparisonParameter();

            var service = new ChecksumComparisonService();
            var comparisonResult = service.compareChecksums(parameter);

            if (comparisonResult.isOk()) {
                var result = comparisonResult.getValue();
                this.processResult(result);
            } else {
                var error = comparisonResult.getError();
                this.reportProblem(error);
            }



            super.enableUi(pnlRoot);
        });
        checksumComparisonThread.start();
    }

    @FXML
    private void openInputFile1() {
        super.openFile(
            "Select the first input file",
            FileDialog::openForReading,
            file -> txtInput1.setText(file.getAbsolutePath())
        );
    }

    @FXML
    private void openInputFile2() {
        super.openFile(
            "Select the second input file",
            FileDialog::openForReading,
            file -> txtInput2.setText(file.getAbsolutePath())
        );
    }

    @FXML
    private void changeAlgorithm(ScrollEvent event) {
        double delta = event.getDeltaY();
        boolean isScrollDown = delta < 0;
        boolean isScrollUp = delta > 0;

        if (isScrollDown) {
            cmbAlgorithm
                .getSelectionModel()
                .selectNext();
        } else if (isScrollUp) {
            cmbAlgorithm
                .getSelectionModel()
                .selectPrevious();
        }
    }



    private void setupAlgorithms() {
        List<Algorithm> algorithms = Algorithm.getAllAscendingSortedByLength();

        cmbAlgorithm
            .getItems()
            .setAll(algorithms);

        cmbAlgorithm
            .getSelectionModel()
            .selectFirst();

        cmbAlgorithm.setConverter(new StringConverter<>() {
            @Override
            public String toString(Algorithm algorithm) {
                return algorithm.getDisplayName();
            }

            @Override
            public Algorithm fromString(String string) {
                return Algorithm.MD5;
            }
        });
    }

    private ChecksumComparisonParameter createChecksumComparisonParameter() {
        var parameter = new ChecksumComparisonParameter();
        parameter.setAlgorithm(cmbAlgorithm.getValue());
        parameter.setProgressTracker(this::trackProgress);

        if (chkInput1.isSelected()) {
            parameter.setInputProblemDetection1(new InputFileProblemDetection(txtInput1.getText()));
            parameter.setGeneratorUpdate1(new FileGeneratorUpdate(txtInput1.getText()));
        } else {
            parameter.setInputProblemDetection1(new InputTextProblemDetection(txtInput1.getText()));
            parameter.setGeneratorUpdate1(new TextGeneratorUpdate(txtInput1.getText()));
        }

        if (chkInput2.isSelected()) {
            parameter.setInputProblemDetection2(new InputFileProblemDetection(txtInput2.getText()));
            parameter.setGeneratorUpdate2(new FileGeneratorUpdate(txtInput2.getText()));
        } else {
            parameter.setInputProblemDetection2(new InputTextProblemDetection(txtInput2.getText()));
            parameter.setGeneratorUpdate2(new TextGeneratorUpdate(txtInput2.getText()));
        }

        return parameter;
    }

    private void reportProblem(String problem) {
        super.showMessageDialog("Hash Tools", "Problem", problem);
    }

    private void processResult(ChecksumComparisonResult result) {
        String equality = result.matches()
            ? "Matches"
            : "Does not match";

        Platform.runLater(() -> lblEquality.setText(equality));
    }



    @Override
    protected void cleanUi() {
        Platform.runLater(() -> lblEquality.setText(""));
    }

    @Override
    synchronized protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }
}
