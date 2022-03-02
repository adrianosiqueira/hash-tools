package hashtools.gui.screen.checker;

import hashtools.core.consumer.CheckerGUISampleContainerConsumer;
import hashtools.core.language.LanguageManager;
import hashtools.core.model.FileExtension;
import hashtools.core.model.SampleContainer;
import hashtools.core.module.checker.CheckerModule;
import hashtools.core.service.FileService;
import hashtools.core.service.HashAlgorithmService;
import hashtools.gui.dialog.FileOpenerDialog;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>
 * Checker screen controller class.
 * </p>
 *
 * @author Adriano Siqueira
 */
public class CheckerController implements Initializable {

    @FXML private VBox       paneRoot;
    @FXML private GridPane   paneForm;
    @FXML private TitledPane titledPane;
    @FXML private ScrollPane scrollPane;
    @FXML private StackPane  paneResult;

    @FXML private TextField fieldInput;
    @FXML private TextField fieldOfficial;

    @FXML private Button buttonOpenInput;
    @FXML private Button buttonOpenOfficial;
    @FXML private Button buttonCheck;

    @FXML private ProgressBar progressBar;
    @FXML private Label       labelResult;

    private Scene  currentScene;
    private Parent currentRoot;

    private boolean needClearResult;


    private boolean isNotReadyToRun() {
        boolean fieldInputIsEmpty    = fieldInput.getText().isBlank();
        boolean fieldOfficialIsEmpty = fieldOfficial.getText().isBlank();

        return fieldInputIsEmpty || fieldOfficialIsEmpty;
    }

    @FXML
    private void analyzeDragContent(DragEvent event) {
        Dragboard      dragboard = event.getDragboard();
        TransferMode[] transferModes;

        if (event.getSource() == fieldInput) {
            transferModes = TransferMode.ANY;
        } else if (dragboard.hasFiles()) {
            Path path = dragboard.getFiles().get(0).toPath();

            transferModes = new FileService().pathHasRequiredExtension(path, FileExtension.HASH)
                            ? TransferMode.ANY
                            : TransferMode.NONE;
        } else {
            transferModes = new HashAlgorithmService().stringHasValidLength(dragboard.getString())
                            ? TransferMode.ANY
                            : TransferMode.NONE;
        }

        event.acceptTransferModes(transferModes);
    }

    private boolean clearIsNotNecessary() {
        return !needClearResult;
    }

    private void clearResult() {
        if (clearIsNotNecessary()) return;

        labelResult.setText("");
        progressBar.setProgress(0.0);
        needClearResult = false;
    }

    @FXML
    private void clearResultWhenTextFieldContentChange(ObservableValue<String> observable, String oldValue, String newValue) {
        clearResult();
    }

    @FXML
    private void moveTooltipWithMouse(MouseEvent event) {
        if (!(event.getSource() instanceof Control control)) return;

        Tooltip tooltip = control.getTooltip();
        tooltip.setX(event.getScreenX() + 10);
        tooltip.setY(event.getScreenY() + 10);
    }

    @FXML
    private void openInputFile(ActionEvent event) {
        FileOpenerDialog fileOpener = new FileOpenerDialog();
        String           title      = LanguageManager.get("Select.input.file");

        Optional.ofNullable(fileOpener.openFile(title, FileExtension.ALL))
                .ifPresent(f -> fieldInput.setText(f.getAbsolutePath()));
    }

    @FXML
    private void openOfficialFile(ActionEvent event) {
        FileOpenerDialog fileOpener = new FileOpenerDialog();
        String           title      = LanguageManager.get("Select.hash.file");

        Optional.ofNullable(fileOpener.openFile(title, FileExtension.HASH))
                .ifPresent(f -> fieldOfficial.setText(f.getAbsolutePath()));
    }

    @FXML
    private void pasteContentFromDragAndDrop(DragEvent event) {
        if (!(event.getSource() instanceof TextField field)) return;

        Dragboard dragboard = event.getDragboard();

        String content = dragboard.hasFiles()
                         ? dragboard.getFiles().get(0).getAbsolutePath()
                         : dragboard.getString();

        field.setText(content);
    }

    private void runCheckerModule() {
        SampleContainer sampleContainer = new CheckerModule(
                fieldInput.getText(),
                fieldOfficial.getText()
        ).call();

        new CheckerGUISampleContainerConsumer(
                progressBar,
                labelResult
        ).accept(sampleContainer);

        needClearResult = true;
    }

    @FXML
    private void runCheckingModule(ActionEvent event) {
        new Thread(() -> {
            if (isNotReadyToRun()) return;

            startSplash();
            runCheckerModule();
            stopSplash();
        }).start();
    }

    @SuppressWarnings("ConstantConditions")
    private void startSplash() {
        currentScene = paneRoot.getScene();
        currentRoot = currentScene.getRoot();

        try {
            currentScene.setRoot(FXMLLoader.load(getClass().getResource("/hashtools/gui/screen/splash/Splash.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSplash() {
        Platform.runLater(() -> currentScene.setRoot(currentRoot));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearResult();
    }
}
