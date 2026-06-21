package hashtools.module.application.service;

import hashtools.view.util.JavaFxLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class ApplicationService {

    public void openCheckerScreen(Consumer<Pane> screenConsumer) {
        this.performScreenOpening(
            "/hashtools/fxml/checker.fxml",
            screenConsumer
        );
    }

    public void openComparatorScreen(Consumer<Pane> screenConsumer) {
        this.performScreenOpening(
            "/hashtools/fxml/comparator.fxml",
            screenConsumer
        );
    }

    public void openGeneratorScreen(Consumer<Pane> screenConsumer) {
        this.performScreenOpening(
            "/hashtools/fxml/generator.fxml",
            screenConsumer
        );
    }



    private void performScreenOpening(String location, Consumer<Pane> screenConsumer) {
        try {
            JavaFxLoader loader = new JavaFxLoader();
            loader.setLocation(location);
            loader.load();
            loader.consumePane(screenConsumer);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
