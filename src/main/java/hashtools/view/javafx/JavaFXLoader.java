package hashtools.view.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JavaFXLoader extends FXMLLoader {

    public void setLocation(String location) {
        URL url = Optional
            .ofNullable(location)
            .map(this.getClass()::getResource)
            .orElse(null);

        super.setLocation(url);
    }

    public void setResources(String resources) {
        ResourceBundle bundle = Optional
            .ofNullable(resources)
            .map(ResourceBundle::getBundle)
            .orElse(null);

        super.setResources(bundle);
    }



    public Scene createScene(double width, double height) {
        return new Scene(
            super.getRoot(),
            width,
            height
        );
    }

    public void consumeScreen(Consumer<Pane> consumer) {
        consumer.accept(super.getRoot());
    }
}