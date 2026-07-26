package hashtools.controller;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.file.FileDialog;
import hashtools.service.ChecksumComparisonService;
import hashtools.strategy.algorithmsource.ComboBoxAlgorithmSource;
import hashtools.strategy.checksumgeneratorupdater.FileChecksumGeneratorUpdate;
import hashtools.strategy.checksumgeneratorupdater.TextChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputFileIdentification;
import hashtools.strategy.inputidentification.InputTextIdentification;
import hashtools.strategy.problemdetection.InputFileProblemDetection;
import hashtools.strategy.problemdetection.InputTextProblemDetection;
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
            super.cleanUi();



            // Data retrieval
            ChecksumComparisonContext context = this.createComparisonContext();



            // Processing
            switch (comparisonService.compareChecksums(context)) {
                case ChecksumComparisonService.Result.Exception exception -> this.processResult(exception);
                case ChecksumComparisonService.Result.Problem problem -> this.processResult(problem);
                case ChecksumComparisonService.Result.Success success -> this.processResult(success);
            }
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
        ChecksumComparisonContext context = new ChecksumComparisonContext();
        context.setAlgorithmSource(new ComboBoxAlgorithmSource(cmbAlgorithm));

        if (chkInput1.isSelected()) {
            context.setInputIdentification1(new InputFileIdentification(txtInput1.getText()));
            context.setChecksumGeneratorUpdate1(new FileChecksumGeneratorUpdate(txtInput1.getText()));
            context.setProblemDetection1(new InputFileProblemDetection(txtInput1.getText()));
        } else {
            context.setInputIdentification1(new InputTextIdentification(txtInput1.getText()));
            context.setChecksumGeneratorUpdate1(new TextChecksumGeneratorUpdate(txtInput1.getText()));
            context.setProblemDetection1(new InputTextProblemDetection(txtInput1.getText()));
        }

        if (chkInput2.isSelected()) {
            context.setInputIdentification2(new InputFileIdentification(txtInput2.getText()));
            context.setChecksumGeneratorUpdate2(new FileChecksumGeneratorUpdate(txtInput2.getText()));
            context.setProblemDetection2(new InputFileProblemDetection(txtInput2.getText()));
        } else {
            context.setInputIdentification2(new InputTextIdentification(txtInput2.getText()));
            context.setChecksumGeneratorUpdate2(new TextChecksumGeneratorUpdate(txtInput2.getText()));
            context.setProblemDetection2(new InputTextProblemDetection(txtInput2.getText()));
        }

        return context;
    }

    private void processResult(ChecksumComparisonService.Result.Exception result) {
        super.logException(result.throwable());
        super.enableUi(pnlRoot);
    }

    private void processResult(ChecksumComparisonService.Result.Problem result) {
        super.showMessageDialog("Hash Tools", "Problem", result.problem());
        super.enableUi(pnlRoot);
    }

    private void processResult(ChecksumComparisonService.Result.Success result) {
        double equality = result
            .result()
            .calculateEquality();

        prgEquality.setProgress(equality);
        enableUi(pnlRoot);
    }



    @Override
    protected void cleanUi() {
        prgEquality.setProgress(0.0);
    }
}
