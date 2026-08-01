package hashtools.window;

import hashtools.controller.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JavaFxLoader {

    private FXMLLoader loader;



    public JavaFxLoader() {
        this.loader = new FXMLLoader();
    }



    public void setLocation(String location) {
        URL url = this
            .getClass()
            .getResource(location);

        loader.setLocation(url);
    }

    public void setResources(String resources) {
        ResourceBundle bundle = ResourceBundle.getBundle(
            resources,
            Locale.getDefault()
        );

        loader.setResources(bundle);
    }

    public void load() throws IOException {
        loader.load();
    }



    public void consumeScene(Consumer<Scene> consumer) {
        Scene scene = new Scene(loader.getRoot());
        consumer.accept(scene);
    }

    public void consumePane(Consumer<Pane> consumer) {
        Pane pane = loader.getRoot();
        consumer.accept(pane);
    }

    public void consumeController(Consumer<AbstractController> consumer) {
        AbstractController controller = loader.getController();
        consumer.accept(controller);
    }
}
