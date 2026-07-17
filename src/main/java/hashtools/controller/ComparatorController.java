package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.container.ChecksumComparisonContainer;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.service.ComparatorService;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.algorithmsource.ComboBoxAlgorithmSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
import hashtools.strategy.thread.VirtualThreadFactory;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
    private ProgressBar prgEquality;

    private ComparatorService comparatorService;
    private ThreadFactory threadFactory;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.comparatorService = new ComparatorService();
        this.threadFactory = new VirtualThreadFactory();
        this.setupAlgorithms();
    }



    @FXML
    private void performChecksumComparison() {
        threadFactory.newThread(() -> {
            // User feedback
            super.disableUi(pnlRoot);
            super.cleanUi();



            // Data retrieval
            InputSource inputSource1 = this.createInputSource1();
            InputSource inputSource2 = this.createInputSource2();
            AlgorithmSource algorithmSource = this.createAlgorithmSource();



            // Communication setup
            ChecksumComparisonContext parameter = new ChecksumComparisonContext();
            parameter.setInputSource1(inputSource1);
            parameter.setInputSource2(inputSource2);
            parameter.setAlgorithmSource(algorithmSource);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumComparisonContainer container = comparatorService.performChecksumComparison(parameter);
            container.consumeResultIfPresent(this::presentResult);
            container.consumeProblemIfPresent(this::showMessageDialog);
            container.consumeExceptionIfPresent(this::logException);
        }).start();
    }

    @FXML
    private void openInputFile1() {
        super.openFile(
            "Select the first input file",
            FileDialog::openForReading,
            file -> txtInput1.setText(file.toString())
        );
    }

    @FXML
    private void openInputFile2() {
        super.openFile(
            "Select the second input file",
            FileDialog::openForReading,
            file -> txtInput2.setText(file.toString())
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



    private InputSource createInputSource1() {
        return chkInput1.isSelected()
            ? new FileInputSource(txtInput1.getText())
            : new TextInputSource(txtInput1.getText());
    }

    private InputSource createInputSource2() {
        return chkInput2.isSelected()
            ? new FileInputSource(txtInput2.getText())
            : new TextInputSource(txtInput2.getText());
    }

    private AlgorithmSource createAlgorithmSource() {
        return new ComboBoxAlgorithmSource(cmbAlgorithm);
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

    private void presentResult(ChecksumComparisonResult result) {
        double equality = result.calculateEquality();
        prgEquality.setProgress(equality);
        enableUi(pnlRoot);
    }

    private void showMessageDialog(String message) {
        super.showMessageDialog("Hash Tools", "Problem", message);
        super.enableUi(pnlRoot);
    }



    @Override
    protected void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }

    @Override
    protected void logException(Exception exception) {
        super.logException(exception);
        super.enableUi(pnlRoot);
    }

    @Override
    protected void cleanUi() {
        prgEquality.setProgress(0.0);
    }
}
