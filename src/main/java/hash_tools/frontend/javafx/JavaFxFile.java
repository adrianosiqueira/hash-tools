package hash_tools.frontend.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JavaFxFile {

    private URL location;
    private ResourceBundle resources;

    private Consumer<Exception> exceptionHandler = this::doNotDoAnything;
    private List<Consumer<Pane>> paneConsumers = new ArrayList<>();
    private List<Consumer<Object>> controllerConsumers = new ArrayList<>();



    public JavaFxFile location(String location) {
        this.location = this
            .getClass()
            .getResource(location);
        return this;
    }

    public JavaFxFile resources(ResourceBundle resources) {
        this.resources = resources;
        return this;
    }

    public JavaFxFile handleException(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = Optional
            .ofNullable(exceptionHandler)
            .orElse(this::doNotDoAnything);
        return this;
    }

    public JavaFxFile consumePane(Consumer<Pane> consumer) {
        paneConsumers.add(consumer);
        return this;
    }

    public <T> JavaFxFile consumeController(Consumer<T> consumer) {
        controllerConsumers.add(controller -> {
            @SuppressWarnings("unchecked")
            T castedController = (T) controller;
            consumer.accept(castedController);
        });
        return this;
    }



    public void process() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(location);
        loader.setResources(resources);

        try {
            Pane pane = loader.load();
            paneConsumers.forEach(c -> c.accept(pane));

            Object controller = loader.getController();
            controllerConsumers.forEach(c -> c.accept(controller));
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }



    private void doNotDoAnything(Exception unused) {
    }
}
