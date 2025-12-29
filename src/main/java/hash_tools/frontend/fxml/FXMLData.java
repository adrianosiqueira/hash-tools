package hash_tools.frontend.fxml;

import javafx.scene.layout.Pane;

import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public record FXMLData(
    Pane pane,
    Object controller
) {

    public FXMLData usePane(Consumer<Pane> consumer) {
        consumer.accept(pane);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> FXMLData useController(Consumer<T> consumer) {
        consumer.accept((T) controller);
        return this;
    }
}