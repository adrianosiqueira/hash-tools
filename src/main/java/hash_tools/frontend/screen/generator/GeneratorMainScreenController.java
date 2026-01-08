package hash_tools.frontend.screen.generator;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.source.ChecksumSource;
import hash_tools.backend.checksum.source.FileChecksumSource;
import hash_tools.backend.checksum.source.StringChecksumSource;
import hash_tools.backend.request.GeneratorRequest;
import hash_tools.backend.request.processor.GeneratorRequestProcessor;
import hash_tools.backend.result.GeneratorResult;
import hash_tools.frontend.abstraction.ProcessingObservable;
import hash_tools.frontend.dialog.FileDialog;
import hash_tools.frontend.dialog.FileExtension;
import hash_tools.frontend.javafx.Execution;
import javafx.css.PseudoClass;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class GeneratorMainScreenController implements Initializable, ProcessingObservable {

    private static final PseudoClass FOCUSED = PseudoClass.getPseudoClass("focused");



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
    private Pane pnlAlgorithm;
    @FXML
    private Label lblAlgorithm;
    @FXML
    private CheckBox chkMd5;
    @FXML
    private CheckBox chkSha1;
    @FXML
    private CheckBox chkSha224;
    @FXML
    private CheckBox chkSha256;
    @FXML
    private CheckBox chkSha384;
    @FXML
    private CheckBox chkSha512;

    @FXML
    private Button btnGenerate;


    private ResourceBundle resources;
    private List<Runnable> startingTasks;
    private List<Runnable> stoppingTasks;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.startingTasks = new ArrayList<>();
        this.stoppingTasks = new ArrayList<>();

        pnlInput
            .focusWithinProperty()
            .addListener((_, _, focused) -> pnlInput.pseudoClassStateChanged(FOCUSED, focused));
    }



    @FXML
    private void handleBtnOpenInputFileAction() {
        openInputFile();
    }

    @FXML
    private void handleBtnGenerateAction() {
        performGenerationOperation();
    }



    private void performGenerationOperation() {
        Runnable generationOperation = () -> new GeneratorRequest()
            .checksumSource(this::createChecksumSource)
            .algorithms(this::createAlgorithmList)
            .process(new GeneratorRequestProcessor())
            .consume(this::consumeResult);

        new Execution()
            .addTask(this::performStartingTasks)
            .addTask(generationOperation)
            .addTask(this::performStoppingTasks)
            .executeSequential();
    }

    private void openInputFile() {
        new FileDialog()
            .title("Select the file to generate")
            .resources(resources)
            .defaultExtension(FileExtension.ALL)
            .ownerWindow(pnlRoot.getScene().getWindow())
            .openFile()
            .map(Path::toString)
            .ifPresent(txtInput::setText);
    }



    private ChecksumSource createChecksumSource() {
        return chkUseInputFile.isSelected()
            ? new FileChecksumSource(Path.of(txtInput.getText()))
            : new StringChecksumSource(txtInput.getText());
    }

    private List<Algorithm> createAlgorithmList() {
        return pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .filter(CheckBox::isSelected)
            .map(CheckBox::getText)
            .map(Algorithm::fromName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    private void consumeResult(GeneratorResult result) {
        result
            .checksums()
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
