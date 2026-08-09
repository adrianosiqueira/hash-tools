package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.file.FileDialog;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.service.ChecksumComparisonService;
import hashtools.strategy.algorithmsource.ComboBoxAlgorithmSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.inputsource.TextInputSource;
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



    @FXML
    private void performChecksumComparison() {
        threadFactory.newThread(() -> {
            // User feedback
            super.disableUi(pnlRoot);
            this.cleanUi();



            // Data retrieval
            ChecksumComparisonContext context = this.createComparisonContext();



            // Processing
            switch (comparisonService.compareChecksums(context)) {
                case CanceledResult _ -> {}
                case ExceptionResult result -> this.processResult(result);
                case ProblemResult result -> this.processResult(result);
                case ChecksumComparisonResult result -> this.processResult(result);
            }



            // User feedback
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

    private ChecksumComparisonContext createComparisonContext() {
        InputSource inputSource1 = chkInput1.isSelected()
            ? new FileInputSource(txtInput1.getText())
            : new TextInputSource(txtInput1.getText());

        InputSource inputSource2 = chkInput2.isSelected()
            ? new FileInputSource(txtInput2.getText())
            : new TextInputSource(txtInput2.getText());



        ChecksumComparisonContext context = new ChecksumComparisonContext();
        context.setInputSource1(inputSource1);
        context.setInputSource2(inputSource2);
        context.setAlgorithmSource(new ComboBoxAlgorithmSource(cmbAlgorithm));

        return context;
    }

    private void processResult(ExceptionResult result) {
        result.consumeException(super::logException);
    }

    private void processResult(ProblemResult result) {
        super.showMessageDialog("Hash Tools", "Problem", result.getDescription());
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
}
