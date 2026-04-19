package hashtools.view;

import hashtools.view.dialog.StackTraceDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationWindow extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWindow.class);

    private static final double WIDTH = 853.0;
    private static final double HEIGHT = 480.0;



    @Override
    public void start(Stage stage) {
        URL location = this
            .getClass()
            .getResource("/hashtools/fxml/main-screen.fxml");

        // TODO Use the real bundle
        ResourceBundle resources = null;



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setResources(resources);
            loader.load();

            Scene scene = new Scene(loader.getRoot(), WIDTH, HEIGHT);
            stage.setScene(scene);

            Closeable controller = loader.getController();
            stage.setOnCloseRequest(_ -> this.closeController(controller));

            LOGGER.info("Loaded main screen '{}'.", location);
        } catch (Exception e) {
            new StackTraceDialog()
                .setTitle("HashTools")
                .setThrowable(e)
                .show();
            LOGGER.error("Failed to load the main screen.", e);
        }



        stage.setTitle("HashTools");
        stage.show();
    }



    private void closeController(Closeable controller) {
        try {
            controller.close();
            LOGGER.debug("Closed the controller '{}'.", controller);
        } catch (Exception e) {
            LOGGER.error("Failed to close the controller '{}'.", controller);
            throw new RuntimeException(e);
        }
    }
}
