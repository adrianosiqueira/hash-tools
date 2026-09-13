package hashtools.service;

import hashtools.controller.AbstractController;
import hashtools.window.JavaFxLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class ApplicationService {

    private Thread shutdownHook;



    public ApplicationService() {
        this.shutdownHook = new Thread(() -> {});
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

    private void swapController(AbstractController controller) {
        try {
            shutdownHook.start();
            shutdownHook.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        var runtime = Runtime.getRuntime();
        runtime.removeShutdownHook(shutdownHook);

        shutdownHook = new Thread(controller::stopAllServicesProcessing);
        runtime.addShutdownHook(shutdownHook);
    }
}
