package hashtools.backend.comparator.controller;

import hashtools.backend.core.checksum.Algorithm;
import hashtools.backend.core.file.EnhancedFile;
import hashtools.backend.core.file.FileDialog;
import hashtools.backend.core.source.InputSource;
import hashtools.backend.core.threadpool.ThreadPool;
import hashtools.backend.comparator.domain.ChecksumComparisonContainer;
import hashtools.backend.comparator.domain.ChecksumComparisonParameter;
import hashtools.backend.comparator.domain.ChecksumComparisonResult;
import hashtools.backend.comparator.service.ComparatorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
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
    private ComboBox<Algorithm> cmbAlgorithm;

    @FXML
    private ProgressBar prgProgress;
    @FXML
    private ProgressBar prgEquality;

    private ComparatorService comparatorService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.comparatorService = new ComparatorService();
        this.setupAlgorithms();
    }



    @FXML
    private void performChecksumComparison() {
        ThreadPool.CACHED_DAEMON.execute(() -> {
            // User feedback
            disableUi(pnlRoot);
            cleanUi();



            // Data retrieval
            InputSource inputSource1 = this.createInputSource1();
            InputSource inputSource2 = this.createInputSource2();
            Algorithm algorithm = this.getSelectedAlgorithm();



            // Communication setup
            ChecksumComparisonParameter parameter = new ChecksumComparisonParameter();
            parameter.setInputSource1(inputSource1);
            parameter.setInputSource2(inputSource2);
            parameter.setAlgorithm(algorithm);
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
            ? InputSource.fileInputSource(txtInput1.getText())
            : InputSource.textInputSource(txtInput1.getText());
    }

    private InputSource createInputSource2() {
        return chkInput2.isSelected()
            ? InputSource.fileInputSource(txtInput2.getText())
            : InputSource.textInputSource(txtInput2.getText());
    }

    private Algorithm getSelectedAlgorithm() {
        return cmbAlgorithm.getValue();
    }



    private void trackProgress(double progress) {
        prgProgress.setProgress(progress);
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

    private void cleanUi() {
        prgEquality.setProgress(0.0);
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
}
