package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.file.FileDialog;
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

    private ChecksumComparisonService comparisonService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.comparisonService = new ChecksumComparisonService();
        this.threadFactory = new VirtualThreadFactory();
        this.setupAlgorithms();
    }

    @Override
    public void stopAllServicesProcessing() {
        comparisonService.cancelChecksumsComparison();
    }



    @FXML
    private void performChecksumComparison() {
        threadFactory.newThread(() -> {
            super.disableUi(pnlRoot);
            this.cleanUi();

            this.setupService();
            comparisonService.compareChecksums();

            super.enableUi(pnlRoot);
        }).start();
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

    private void setupService() {
        comparisonService.initSetup();
        comparisonService.setExceptionConsumer(super::logException);
        comparisonService.setProblemConsumer(this::reportProblem);
        comparisonService.setProgressConsumer(this::trackProgress);
        comparisonService.setResultConsumer(this::processResult);
        comparisonService.setAlgorithm(cmbAlgorithm.getValue());



        if (chkInput1.isSelected()) {
            comparisonService.setInputProblemDetection1(new InputFileProblemDetection(txtInput1.getText()));
            comparisonService.setGeneratorUpdate1(new FileGeneratorUpdate(txtInput1.getText()));
        } else {
            comparisonService.setInputProblemDetection1(new InputTextProblemDetection(txtInput1.getText()));
            comparisonService.setGeneratorUpdate1(new TextGeneratorUpdate(txtInput1.getText()));
        }



        if (chkInput2.isSelected()) {
            comparisonService.setInputProblemDetection2(new InputFileProblemDetection(txtInput2.getText()));
            comparisonService.setGeneratorUpdate2(new FileGeneratorUpdate(txtInput2.getText()));
        } else {
            comparisonService.setInputProblemDetection2(new InputTextProblemDetection(txtInput2.getText()));
            comparisonService.setGeneratorUpdate2(new TextGeneratorUpdate(txtInput2.getText()));
        }
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
