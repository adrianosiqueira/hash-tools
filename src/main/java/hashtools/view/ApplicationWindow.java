package hashtools.view;

import hashtools.view.dialog.StackTraceDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApplicationWindow extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWindow.class);

    private static final double WIDTH = 853.0;
    private static final double HEIGHT = 480.0;



    @Override
    public void start(Stage stage) {
        try {
            Scene scene = this.createScene(
                "/hashtools/fxml/main-screen.fxml",
                null
            );

            stage.setScene(scene);
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



    private Scene createScene(String screenPath, String resourcesBasename) throws Exception {
        URL location = this
            .getClass()
            .getResource(screenPath);

        ResourceBundle resources = Optional
            .ofNullable(resourcesBasename)
            .map(ResourceBundle::getBundle)
            .orElse(null);



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setResources(resources);
        loader.load();



        return new Scene(
            loader.getRoot(),
            WIDTH,
            HEIGHT
        );
    }
}
