package hash_tools.frontend.screen.checker;

import hash_tools.backend.checksum.CheckingChecksum;
import hash_tools.backend.checksum.extractor.ChecksumExtractor;
import hash_tools.backend.checksum.extractor.FileChecksumExtractor;
import hash_tools.backend.checksum.extractor.StringChecksumExtractor;
import hash_tools.backend.checksum.source.ChecksumSource;
import hash_tools.backend.checksum.source.FileChecksumSource;
import hash_tools.backend.checksum.source.StringChecksumSource;
import hash_tools.backend.request.CheckerRequest;
import hash_tools.backend.request.processor.CheckerRequestProcessor;
import hash_tools.backend.result.CheckerResult;
import hash_tools.frontend.abstraction.ProcessingObservable;
import hash_tools.frontend.dialog.FileDialog;
import hash_tools.frontend.dialog.FileExtension;
import hash_tools.frontend.javafx.AsyncRunner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CheckerMainScreenController implements Initializable, ProcessingObservable {

    @FXML
    private Pane pnlRoot;

    @FXML
    private Pane pnlInput;
    @FXML
    private Label lblInput;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnOpenInputFile;
    @FXML
    private CheckBox chkUseInputFile;

    @FXML
    private Pane pnlChecksum;
    @FXML
    private Label lblChecksum;
    @FXML
    private TextField txtChecksum;
    @FXML
    private Button btnOpenChecksumFile;
    @FXML
    private CheckBox chkUseChecksumFile;

    @FXML
    private Button btnCheck;


    private ResourceBundle resources;
    private AsyncRunner runner;
    private List<Runnable> startingTasks;
    private List<Runnable> stoppingTasks;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.runner = new AsyncRunner();
        this.startingTasks = new ArrayList<>();
        this.stoppingTasks = new ArrayList<>();
    }



    @FXML
    private void performCheckingOperation() {
        Runnable runnable = () -> CheckerRequest
            .createUsingSuppliers(
                this::createChecksumSource,
                this::createChecksumExtractor)
            .process(new CheckerRequestProcessor())
            .consume(this::consumeResult);

        runner.runAsync(
            this::performStartingTasks,
            runnable,
            this::performStoppingTasks
        );
    }

    @FXML
    private void openInputFile() {
        new FileDialog()
            .title("Select the file to check")
            .resources(resources)
            .ownerWindow(pnlRoot.getScene().getWindow())
            .defaultExtension(FileExtension.ALL)
            .openFile()
            .map(Path::toString)
            .ifPresent(txtInput::setText);
    }

    @FXML
    private void openChecksumFile() {
        new FileDialog()
            .title("Select the checksums file")
            .resources(resources)
            .ownerWindow(pnlRoot.getScene().getWindow())
            .defaultExtension(FileExtension.CHECKSUM)
            .openFile()
            .map(Path::toString)
            .ifPresent(txtInput::setText);
    }



    private ChecksumSource createChecksumSource() {
        return chkUseInputFile.isSelected()
            ? new FileChecksumSource(Path.of(txtInput.getText()))
            : new StringChecksumSource(txtInput.getText());
    }

    private ChecksumExtractor createChecksumExtractor() {
        return chkUseChecksumFile.isSelected()
            ? new FileChecksumExtractor(Path.of(txtChecksum.getText()))
            : new StringChecksumExtractor(txtChecksum.getText());
    }

    private void consumeResult(CheckerResult result) {
        result
            .checksums()
            .stream()
            .map(CheckingChecksum::toString)
            .forEach(IO::println);
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
