package hash_tools.frontend.screen.checker;

import hash_tools.frontend.abstraction.ClosingObservable;
import hash_tools.frontend.dialog.FileDialog;
import hash_tools.frontend.dialog.FileExtension;
import hash_tools.backend.checksum.extractor.ChecksumExtractor;
import hash_tools.backend.checksum.extractor.FileChecksumExtractor;
import hash_tools.backend.checksum.extractor.StringChecksumExtractor;
import hash_tools.backend.checksum.source.ChecksumSource;
import hash_tools.backend.checksum.source.FileChecksumSource;
import hash_tools.backend.checksum.source.StringChecksumSource;
import hash_tools.backend.request.CheckerRequest;
import hash_tools.backend.request.processor.CheckerRequestProcessor;
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

public class MainScreenController implements Initializable, ClosingObservable {

    @FXML
    private Pane pnlRoot;

    @FXML
    private Pane pnlHeader;
    @FXML
    private Button btnBack;
    @FXML
    private Label lblHeader;

    @FXML
    private Pane pnlContent;
    @FXML
    private Label lblInput;
    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkUseInputFile;
    @FXML
    private Button btnOpenInputFile;

    @FXML
    private Label lblChecksum;
    @FXML
    private TextField txtChecksum;
    @FXML
    private CheckBox chkUseChecksumFile;
    @FXML
    private Button btnOpenChecksumFile;

    @FXML
    private Button btnRun;


    private ResourceBundle resources;
    private List<Runnable> closingTasks;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.closingTasks = new ArrayList<>();


        btnOpenInputFile.disableProperty().bind(chkUseInputFile.selectedProperty().not());
        btnOpenChecksumFile.disableProperty().bind(chkUseChecksumFile.selectedProperty().not());
    }



    @FXML
    private void close() {
        performClosingTasks();
    }

    @FXML
    private void performCheckingOperation() {
        // TODO Properly consume the result
        CheckerRequest
                .createUsingSuppliers(
                        this::createChecksumSource,
                        this::createChecksumExtractor)
                .process(new CheckerRequestProcessor())
                .consume(IO::println);
    }

    @FXML
    private void openInputFile() {
        FileDialog
                .startSetup()
                .localized(resources)
                .title("Select the file to check")
                .addExtensionFilter(FileExtension.ALL)
                .addExtensionFilter(FileExtension.CHECKSUM)
                .openFile()
                .map(Path::toString)
                .ifPresent(txtInput::setText);
    }

    @FXML
    private void openChecksumFile() {
        FileDialog
                .startSetup()
                .localized(resources)
                .title("Select the checksums file")
                .addExtensionFilter(FileExtension.CHECKSUM)
                .addExtensionFilter(FileExtension.ALL)
                .openFile()
                .map(Path::toString)
                .ifPresent(txtChecksum::setText);
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



    @Override
    public void performWhenClosed(Runnable runnable) {
        closingTasks.add(runnable);
    }

    @Override
    public void performClosingTasks() {
        closingTasks.forEach(Runnable::run);
    }
}
