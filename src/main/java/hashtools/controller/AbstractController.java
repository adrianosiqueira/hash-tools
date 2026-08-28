package hashtools.controller;

import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileDialog;
import hashtools.window.MessageDialog;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void stopAllServicesProcessing() {
    }



    protected void disableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.WAIT);
            pane.getChildren().forEach(this::disableUi);
        } else if (node instanceof Control control) {
            control.setDisable(true);
        }
    }

    protected void enableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.DEFAULT);
            pane.getChildren().forEach(this::enableUi);
        } else if (node instanceof Control control) {
            control.setDisable(false);
        }
    }

    protected void cleanUi() {
    }



    protected void showMessageDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            MessageDialog dialog = new MessageDialog();
            dialog.setTitle(title);
            dialog.setHeader(header);
            dialog.setMessage(content);
            dialog.show();
        });
    }

    protected void logException(Throwable throwable) {
    }

    protected void trackProgress(double progress) {
    }



    protected void openFile(String title, Function<FileDialog, Optional<EnhancedFile>> openningFunction, Consumer<EnhancedFile> fileConsumer) {
        FileDialog fileDialog = new FileDialog()
            .withTitle(title);

        openningFunction
            .apply(fileDialog)
            .ifPresent(fileConsumer);
    }

    protected Optional<EnhancedFile> openFile(String title, Function<FileDialog, Optional<EnhancedFile>> openningFunction) {
        FileDialog dialog = new FileDialog().withTitle(title);

        return openningFunction.apply(dialog);
    }
}
