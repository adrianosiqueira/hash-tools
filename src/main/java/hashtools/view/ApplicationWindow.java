package hashtools.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL location = this
            .getClass()
            .getResource("/hashtools/fxml/main-screen.fxml");

        ResourceBundle resources = null;

        Scene scene = this.createScene(
            location,
            resources
        );



        stage.setScene(scene);
        stage.setTitle("HashTools");
        stage.show();
    }



    private Scene createScene(URL location, ResourceBundle resources) throws Exception {
        Pane pane = FXMLLoader.load(
            location,
            resources
        );

        return new Scene(pane, 853.0, 480.0);
    }
}
