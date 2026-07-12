package hashtools.backend.application.service;

import hashtools.backend.core.interfaces.Controller;
import hashtools.backend.core.strategy.controller.NullController;
import hashtools.frontend.util.JavaFxLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class ApplicationService {

    private Controller activeController;



    public ApplicationService() {
        this.activeController = new NullController();
    }



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
            loader.consumeController(this::swapController);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void swapController(Controller controller) {
        activeController.close();
        activeController = controller;
    }
}
