package hashtools.view;

import hashtools.view.dialog.MessageDialog;
import hashtools.view.javafx.JavaFXLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            new MessageDialog()
                .withTitle("HashTools")
                .withHeader("Failed to load the main screen.")
                .withThrowable(e)
                .show();

            LOGGER.error("Failed to load the main screen.", e);
        }



        stage.setTitle("HashTools");
        stage.show();
    }



    private Scene createScene(String screenLocation, String resourcesBaseName) throws Exception {
        JavaFXLoader loader = new JavaFXLoader();
        loader.setLocation(screenLocation);
        loader.setResources(resourcesBaseName);
        loader.load();

        return loader.createScene(
            WIDTH,
            HEIGHT
        );
    }
}
