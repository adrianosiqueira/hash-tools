package hashtools.module.main.controller;

import hashtools.view.dialog.MessageDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Closeable, Initializable {

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
        this.openCheckingScreen();
    }

    @Override
    public void close() throws IOException {
        // FIXME Maybe it is not necessary
        currentOpenController.close();
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
            return;
        }



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Pane screen = loader.load();

            currentOpenController.close();
            currentOpenController = loader.getController();

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
