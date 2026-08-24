package hashtools.window;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MessageDialog {

    @FXML
    private Label headerLabel;
    @FXML
    private Label messageLabel;

    private Stage window;



    public MessageDialog() {
        double initialWidth = Screen
            .getPrimary()
            .getBounds()
            .getWidth()
            * 0.25;



        this.window = new Stage();
        window.setWidth(initialWidth);

        try {
            JavaFxLoader loader = new JavaFxLoader();
            loader.setLocation("/hashtools/fxml/message-dialog.fxml");
            loader.setController(this);
            loader.load();
            loader.consumeScene(window::setScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    private void close() {
        window.close();
    }



    public void show() {
        window.show();
    }

    public void showAndWait() {
        window.showAndWait();
    }

    public void setTitle(String title) {
        window.setTitle(title);
    }

    public void setHeader(String header) {
        headerLabel.setText(header);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
