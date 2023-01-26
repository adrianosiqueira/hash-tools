package hashtools.gui.window.application;

import hashtools.core.consumer.data.GUIDataConsumer;
import hashtools.core.model.Data;
import hashtools.core.model.FileExtension;
import hashtools.gui.dialog.FileOpenerDialog;
import hashtools.gui.window.AbstractController;
import hashtools.gui.window.about.AboutController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

/**
 * <p>
 * Application screen controller class.
 * </p>
 *
 * @author Adriano Siqueira
 */
@SuppressWarnings("unused")
public class ApplicationController extends AbstractController {

    private final String           buttonHighlightStyleClass;
    private final BooleanProperty  checking;
    private final FileOpenerDialog fileOpenerDialog;

    @FXML
    private BorderPane paneRoot;
    @FXML
    private VBox       paneLeft;
    @FXML
    private HBox       paneProgress;
    @FXML
    private GridPane   paneCenter;
    @FXML
    private GridPane   paneAlgorithm;
    @FXML
    private TitledPane paneDetail;

    @FXML
    private MenuBar  menuBar;
    @FXML
    private Menu     menuFile;
    @FXML
    private Menu     menuHelp;
    @FXML
    private MenuItem itemClose;
    @FXML
    private MenuItem itemOnlineManual;
    @FXML
    private MenuItem itemAbout;

    @FXML
    private Button buttonCheck;
    @FXML
    private Button buttonGenerate;
    @FXML
    private Button buttonRun;
    @FXML
    private Button buttonOpenInputFile;
    @FXML
    private Button buttonOpenOfficialFile;
    @FXML
    private Button buttonOpenOutputFile;

    @FXML
    private Label labelInput;
    @FXML
    private Label labelOfficial;
    @FXML
    private Label labelOutput;
    @FXML
    private Label labelAlgorithm;

    @FXML
    private TextField fieldInput;
    @FXML
    private TextField fieldOfficial;
    @FXML
    private TextField fieldOutput;
    @FXML
    private TextArea  areaDetail;

    @FXML
    private CheckBox checkInputFile;
    @FXML
    private CheckBox checkOfficialFile;
    @FXML
    private CheckBox checkMd5;
    @FXML
    private CheckBox checkSha1;
    @FXML
    private CheckBox checkSha224;
    @FXML
    private CheckBox checkSha256;
    @FXML
    private CheckBox checkSha384;
    @FXML
    private CheckBox checkSha512;

    @FXML
    private ProgressBar progressBar;

    private Stage stage;

    public ApplicationController() {
        super.fxmlPath       = "Application.fxml";
        super.stylesheetPath = "Application.css";

        this.buttonHighlightStyleClass = "button-highlight";
        this.checking                  = new SimpleBooleanProperty(false);
        this.fileOpenerDialog          = new FileOpenerDialog();
    }

    private void close() {
        stage.close();
    }

    private void configureActions() {
        itemAbout.setOnAction(e -> openAboutDialog());
        itemClose.setOnAction(e -> close());
        itemOnlineManual.setOnAction(e -> openOnlineManual());
        buttonCheck.setOnAction(e -> enableCheckMode());
        buttonGenerate.setOnAction(e -> enableGenerateMode());
        buttonRun.setOnAction(e -> run());
        buttonOpenInputFile.setOnAction(e -> openInputFile());
        buttonOpenOfficialFile.setOnAction(e -> openOfficialFile());
        buttonOpenOutputFile.setOnAction(e -> openOutputFile());

        checkInputFile.selectedProperty().addListener((observable, oldValue, newValue) -> buttonOpenInputFile.setDisable(!newValue));
        checkOfficialFile.selectedProperty().addListener((observable, oldValue, newValue) -> buttonOpenOfficialFile.setDisable(!newValue));
    }

    private void configureRunningMode() {
        checking.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // checking
                buttonCheck.getStyleClass().add(buttonHighlightStyleClass);
                buttonGenerate.getStyleClass().remove(buttonHighlightStyleClass);

                fieldOfficial.setDisable(false);
                checkOfficialFile.setDisable(false);
                buttonOpenOfficialFile.setDisable(!checkOfficialFile.isSelected());

                paneAlgorithm.setDisable(true);
            } else {
                // generating
                buttonCheck.getStyleClass().remove(buttonHighlightStyleClass);
                buttonGenerate.getStyleClass().add(buttonHighlightStyleClass);

                fieldOfficial.setDisable(true);
                checkOfficialFile.setDisable(true);
                buttonOpenOfficialFile.setDisable(true);

                paneAlgorithm.setDisable(false);
            }
        });
    }

    private void configureStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(paneRoot));
        stage.show();
    }

    private Data createData() {
        Data data = new Data();

        if (isInCheckRunningMode()) {
            data.setChecking();

            if (isUsingOfficialFile()) {
                data.setOfficialFile(getOfficialFile());
            } else {
                data.setOfficialHash(getOfficialHash());
            }
        } else {
            data.setGenerating();
            data.setAlgorithms(getSelectedAlgorithms());
        }

        if (isUsingInputFile()) {
            data.setInputFile(getInputFile());
        } else {
            data.setInputText(getInputText());
        }

        if (hasOutputFile()) {
            data.setOutputFile(getOutputFile());
        }

        data.setConsumers(new GUIDataConsumer(areaDetail));
        return data;
    }

    private void enableCheckMode() {
        checking.set(true);
    }

    private void enableGenerateMode() {
        checking.set(false);
    }

    private Path getInputFile() {
        return Path.of(fieldInput.getText());
    }

    private String getInputText() {
        return fieldInput.getText();
    }

    private Path getOfficialFile() {
        return Path.of(fieldOfficial.getText());
    }

    private String getOfficialHash() {
        return fieldOfficial.getText();
    }

    private Path getOutputFile() {
        return Path.of(fieldOutput.getText());
    }

    private String[] getSelectedAlgorithms() {
        return paneAlgorithm.getChildren()
                            .stream()
                            .filter(CheckBox.class::isInstance)
                            .map(CheckBox.class::cast)
                            .filter(CheckBox::isSelected)
                            .map(CheckBox::getText)
                            .toArray(String[]::new);
    }

    private boolean hasOutputFile() {
        return !fieldOutput.getText().isBlank();
    }

    private boolean isInCheckRunningMode() {
        return checking.get();
    }

    private boolean isUsingInputFile() {
        return checkInputFile.isSelected();
    }

    private boolean isUsingOfficialFile() {
        return checkOfficialFile.isSelected();
    }

    @Override
    public void launch(Stage stage) {
        loadLogger(this);
        loadFxml(this);
        loadFavIcon(stage, "/hashtools/gui/image/application-icon.png");
        loadWebService(stage);

        configureActions();
        configureRunningMode();
        configureStage(stage);

        enableCheckMode();
    }

    private void openAboutDialog() {
        Stage stage = new Stage();
        stage.getProperties().put("host.services", webService.getHostServices());

        new AboutController().launch(stage);
    }

    private void openInputFile() {
        File file = fileOpenerDialog.openFile("Select the file to check", FileExtension.ALL);
        fieldInput.setText(file.getAbsolutePath());
    }

    private void openOfficialFile() {
        File file = fileOpenerDialog.openFile("Select the file with official hashes", FileExtension.HASH);
        fieldOfficial.setText(file.getAbsolutePath());
    }

    private void openOnlineManual() {
        webService.openWebPage("https://github.com/AdrianoSiqueira/hash-tools/wiki");
    }

    private void openOutputFile() {
        File file = fileOpenerDialog.openFile("Select the output file", FileExtension.ALL);
        fieldOutput.setText(file.getAbsolutePath());
    }

    private void run() {
        Data data = createData();
        System.out.println(data);

//        new CoreRunner(data).run();
    }
}
