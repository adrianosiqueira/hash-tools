package hashtools.module.main.controller;

import hashtools.view.dialog.MessageDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Closeable, Initializable {

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



    private Closeable currentOpenController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currentOpenController = this::doNothing;
        LOGGER.debug("Current open controller set to 'nothing'.");

        this.openCheckingScreen();
    }

    @Override
    public void close() throws IOException {
        // FIXME Maybe it is not necessary
        currentOpenController.close();
        LOGGER.debug("Closed the controller '{}'.", currentOpenController);
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

    private void openScreen(String path) {
        URL location = this
            .getClass()
            .getResource(path);

        if (location == null) {
            LOGGER.error("Location not found: '{}'.", path);
            return;
        }



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Pane screen = loader.load();
            LOGGER.debug("Loaded screen '{}'.", location);

            currentOpenController.close();
            LOGGER.debug("Closed the controller '{}'.", currentOpenController);

            currentOpenController = loader.getController();
            LOGGER.debug("Current open controller set to '{}'.", currentOpenController);

            pnlContent
                .getChildren()
                .setAll(screen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void doNothing() {
    }
}
