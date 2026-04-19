package hashtools.view;

import hashtools.module.main.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationWindow extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWindow.class);



    @Override
    public void start(Stage stage) throws Exception {
        URL location = this
            .getClass()
            .getResource("/hashtools/fxml/main-screen.fxml");

        ResourceBundle resources = null;

        LoadingResult loadingResult = this.loadFxml(
            location,
            resources
        );



        Scene scene = loadingResult.scene();
        Closeable controller = loadingResult.controller();



        stage.setScene(scene);
        stage.setTitle("HashTools");
        stage.show();
        stage.setOnCloseRequest(_ -> this.closeController(controller));
    }



    private LoadingResult loadFxml(URL location, ResourceBundle resources) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setResources(resources);

        Pane pane = loader.load();
        LOGGER.debug("Loaded screen '{}'.", location);

        Scene scene = new Scene(pane, 853.0, 480.0);
        MainController controller = loader.getController();

        return new LoadingResult(scene, controller);
    }

    private void closeController(Closeable controller) {
        try {
            controller.close();
            LOGGER.debug("Closed the controller: '{}'.", controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private record LoadingResult(
        Scene scene,
        Closeable controller
    ) {}
}
