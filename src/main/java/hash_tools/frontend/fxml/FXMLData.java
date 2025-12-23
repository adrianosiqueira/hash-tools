package hash_tools.frontend.fxml;

import javafx.scene.layout.Pane;

import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public record FXMLData<CONTROLLER_TYPE>(Pane pane, CONTROLLER_TYPE controller) {

    public FXMLData<CONTROLLER_TYPE> usePane(Consumer<Pane> consumer) {
        consumer.accept(pane);
        return this;
    }

    public FXMLData<CONTROLLER_TYPE> useController(Consumer<CONTROLLER_TYPE> consumer) {
        consumer.accept(controller);
        return this;
    }
}