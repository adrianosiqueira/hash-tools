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
            .getResource("/hashtools/fxml/application-ui.fxml");

        ResourceBundle resources = null;
//        ResourceBundle resources = ResourceBundle.getBundle(
//            "/hashtools/i18n/i18n.properties",
//            Locale.getDefault()
//        );



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

        return new Scene(pane);
    }
}
