package hash_tools.frontend.screen.comparator;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.source.ChecksumSource;
import hash_tools.backend.checksum.source.FileChecksumSource;
import hash_tools.backend.checksum.source.StringChecksumSource;
import hash_tools.backend.request.ComparatorRequest;
import hash_tools.backend.request.processor.ComparatorRequestProcessor;
import hash_tools.backend.result.ComparatorResult;
import hash_tools.frontend.abstraction.ProcessingObservable;
import hash_tools.frontend.dialog.FileDialog;
import hash_tools.frontend.dialog.FileExtension;
import hash_tools.frontend.javafx.Execution;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComparatorMainScreenController implements Initializable, ProcessingObservable {

    @FXML
    private Pane pnlRoot;

    @FXML
    private Pane pnlInput1;
    @FXML
    private Label lblInput1;
    @FXML
    private TextField txtInput1;
    @FXML
    private Button btnOpenInputFile1;
    @FXML
    private CheckBox chkUseInputFile1;

    @FXML
    private Pane pnlInput2;
    @FXML
    private Label lblInput2;
    @FXML
    private TextField txtInput2;
    @FXML
    private Button btnOpenInputFile2;
    @FXML
    private CheckBox chkUseInputFile2;

    @FXML
    private Label lblPrecision;
    @FXML
    private Slider sldPrecision;

    @FXML
    private Button btnCompare;


    private ResourceBundle resources;
    private List<Runnable> startingTasks;
    private List<Runnable> stoppingTasks;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.startingTasks = new ArrayList<>();
        this.stoppingTasks = new ArrayList<>();

        sldPrecision.setLabelFormatter(formatSliderPrecisionLabels());
    }



    @FXML
    private void performComparisonOperation() {
        Runnable comparisonOperation = () -> new ComparatorRequest()
            .checksumSource1(this::createChecksumSource1)
            .checksumSource2(this::createChecksumSource2)
            .algorithm(this::determineAlgorithm)
            .process(new ComparatorRequestProcessor())
            .consume(this::consumeResult);

        new Execution()
            .addTask(this::performStartingTasks)
            .addTask(comparisonOperation)
            .addTask(this::performStoppingTasks)
            .executeSequential();
    }

    @FXML
    private void openInputFile1() {
        new FileDialog()
            .title("Select the first file to compare")
            .resources(resources)
            .defaultExtension(FileExtension.ALL)
            .ownerWindow(pnlRoot.getScene().getWindow())
            .openFile()
            .map(Path::toString)
            .ifPresent(txtInput1::setText);
    }

    @FXML
    private void openInputFile2() {
        new FileDialog()
            .title("Select the second file to compare")
            .resources(resources)
            .defaultExtension(FileExtension.ALL)
            .ownerWindow(pnlRoot.getScene().getWindow())
            .openFile()
            .map(Path::toString)
            .ifPresent(txtInput2::setText);
    }

    @FXML
    private void changePrecision(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            raisePrecision();
        } else if (event.getDeltaY() < 0) {
            lowerPrecision();
        }

        event.consume();
    }



    private ChecksumSource createChecksumSource1() {
        return chkUseInputFile1.isSelected()
            ? new FileChecksumSource(Path.of(txtInput1.getText()))
            : new StringChecksumSource(txtInput1.getText());
    }

    private ChecksumSource createChecksumSource2() {
        return chkUseInputFile2.isSelected()
            ? new FileChecksumSource(Path.of(txtInput2.getText()))
            : new StringChecksumSource(txtInput2.getText());
    }

    private Algorithm determineAlgorithm() {
        int precision = (int) sldPrecision.getValue();

        return switch (precision) {
            case 0 -> Algorithm.MD5;
            case 1 -> Algorithm.SHA1;
            case 2 -> Algorithm.SHA224;
            case 3 -> Algorithm.SHA256;
            case 4 -> Algorithm.SHA384;
            case 5 -> Algorithm.SHA512;
            default -> throw new RuntimeException("Invalid precision value: " + precision);
        };
    }

    private void consumeResult(ComparatorResult result) {
        String reportContent = String.format(
            "%s  %s\n%s  %s",
            result.checksum1().value(),
            result.identification1(),
            result.checksum2().value(),
            result.identification2()
        );

        IO.println(reportContent);
    }



    private void lowerPrecision() {
        int newValue = Math.max(
            (int) sldPrecision.getMin(),
            (int) sldPrecision.getValue() - 1
        );

        sldPrecision.setValue(newValue);
    }

    private void raisePrecision() {
        int newValue = Math.min(
            (int) sldPrecision.getMax(),
            (int) sldPrecision.getValue() + 1
        );

        sldPrecision.setValue(newValue);
    }



    private StringConverter<Double> formatSliderPrecisionLabels() {
        return new StringConverter<>() {
            @Override
            public String toString(Double object) {
                if (object.equals(sldPrecision.getMin())) {
                    return "Fast";
                } else if (object.equals(sldPrecision.getMax())) {
                    return "Precise";
                } else {
                    return "";
                }
            }

            @Override
            public Double fromString(String string) {
                return 0.0;
            }
        };
    }



    @Override
    public void performWhenProcessingStarts(Runnable runnable) {
        startingTasks.add(runnable);
    }

    private void performStartingTasks() {
        startingTasks.forEach(Runnable::run);
    }

    @Override
    public void performWhenProcessingStops(Runnable runnable) {
        stoppingTasks.add(runnable);
    }

    private void performStoppingTasks() {
        stoppingTasks.forEach(Runnable::run);
    }
}
