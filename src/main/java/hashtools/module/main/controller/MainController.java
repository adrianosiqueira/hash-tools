package hashtools.module.main.controller;

import hashtools.view.dialog.MessageDialog;
import hashtools.view.dialog.StackTraceDialog;
import hashtools.view.javafx.JavaFXLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);



    @FXML
    private Pane pnlRoot;



    @FXML
    private Pane pnlButtons;

    @FXML
    private Button btnChecking;
    @FXML
    private Button btnGeneration;
    @FXML
    private Button btnComparison;


    @FXML
    private Pane pnlContent;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.openCheckingScreen();
    }



    @FXML
    private void openCheckingScreen() {
        this.openScreen("/hashtools/fxml/checking-screen.fxml");
    }

    @FXML
    private void openGenerationScreen() {
        new MessageDialog()
            .setTitle("Main Screen")
            .setMessage("It is not implemented yet.")
            .show();
    }

    @FXML
    private void openComparisonScreen() {
        new MessageDialog()
            .setTitle("Main Screen")
            .setMessage("It is not implemented yet.")
            .show();
    }

    private void openScreen(String location) {
        try {
            JavaFXLoader loader = new JavaFXLoader();
            loader.setLocation(location);
            loader.load();
            loader.consumeScreen(pnlContent.getChildren()::setAll);
            LOGGER.debug("Loaded screen '{}'.", location);
        } catch (Exception e) {
            new StackTraceDialog()
                .setTitle("Screen Opening")
                .setMessage("Failed to open the screen: " + location)
                .setThrowable(e)
                .show();

            LOGGER.error("Failed to open screen '{}'.", location, e);
        }
    }
}
