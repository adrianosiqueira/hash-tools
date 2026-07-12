package hashtools.frontend.util;

import hashtools.backend.core.interfaces.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JavaFxLoader extends FXMLLoader {

    public void setLocation(String location) {
        URL url = this
            .getClass()
            .getResource(location);

        super.setLocation(url);
    }

    public void setResources(String resources) {
        ResourceBundle bundle = ResourceBundle.getBundle(
            resources,
            Locale.getDefault()
        );

        super.setResources(bundle);
    }



    public void consumeScene(Consumer<Scene> consumer) {
        Scene scene = new Scene(this.getRoot());
        consumer.accept(scene);
    }

    public void consumePane(Consumer<Pane> consumer) {
        Pane pane = this.getRoot();
        consumer.accept(pane);
    }

    public void consumeController(Consumer<Controller> consumer) {
        Controller controller = this.getController();
        consumer.accept(controller);
    }
}
