package hashtools.domain.main.controller;

import hashtools.view.dialog.MessageDialog;
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
            .withTitle("Main Screen")
            .withContent("It is not implemented yet.")
            .show();
    }

    @FXML
    private void openComparisonScreen() {
        new MessageDialog()
            .withTitle("Main Screen")
            .withContent("It is not implemented yet.")
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
            new MessageDialog()
                .withTitle("Screen Opening")
                .withHeader("Failed to open the screen: " + location)
                .withContent(e)
                .show();

            LOGGER.error("Failed to open screen '{}'.", location, e);
        }
    }
}
