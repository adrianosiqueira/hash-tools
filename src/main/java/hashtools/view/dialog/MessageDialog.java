package hashtools.view.dialog;

import hashtools.core.model.Problem;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageDialog {

    private String title;
    private String header;
    private Node content;

    private int autoCloseTime;
    private TimeUnit autoCloseTimeUnit;
    private Thread autoCloseThread;



    public MessageDialog() {
        this.title = "";
        this.header = "";
        this.content = null;

        this.autoCloseTime = Integer.MAX_VALUE;
        this.autoCloseTimeUnit = TimeUnit.DAYS;
    }



    public MessageDialog withTitle(String title) {
        this.title = Objects.requireNonNullElse(title, "");
        return this;
    }

    public MessageDialog withHeader(String header) {
        this.header = Objects.requireNonNullElse(header, "");
        return this;
    }

    public MessageDialog withContent(String message) {
        if (message == null) {
            return this;
        }



        String fxml = "/hashtools/fxml/message-dialog-message-screen.fxml";
        URL location = this.getClass().getResource(fxml);

        if (location == null) {
            throw new RuntimeException("FXML not found: " + fxml);
        }



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            content = loader.load();

            ObservableMap<String, Object> namespace = loader.getNamespace();
            ((Label) namespace.get("lblMessage")).setText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public MessageDialog withContent(Throwable throwable) {
        if (throwable == null) {
            return this;
        }



        StringWriter content = new StringWriter();
        throwable.printStackTrace(new PrintWriter(content));



        String fxml = "/hashtools/fxml/message-dialog-throwable-screen.fxml";
        URL location = this.getClass().getResource(fxml);

        if (location == null) {
            throw new RuntimeException("FXML not found: " + fxml);
        }



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            this.content = loader.load();

            ObservableMap<String, Object> namespace = loader.getNamespace();
            ((TextArea) namespace.get("txtContent")).setText(content.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public MessageDialog withContent(Problem problem) {
        if (problem == null) {
            return this;
        }



        String fxml = "/hashtools/fxml/message-dialog-problem-screen.fxml";
        URL location = this.getClass().getResource(fxml);

        if (location == null) {
            throw new RuntimeException("FXML not found: " + fxml);
        }



        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            content = loader.load();

            ObservableMap<String, Object> namespace = loader.getNamespace();
            ((Label) namespace.get("lblDescription")).setText(problem.getDescription());
            ((Label) namespace.get("lblCause")).setText(problem.getCause());
            ((Label) namespace.get("lblFix")).setText(problem.getFix());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public MessageDialog withAutoClose(int autoCloseTime, TimeUnit autoCloseTimeUnit) {
        if (autoCloseTimeUnit == null) {
            return this;
        }



        this.autoCloseTime = Math.abs(autoCloseTime);
        this.autoCloseTimeUnit = autoCloseTimeUnit;

        return this;
    }



    public void show() {
        if (Platform.isFxApplicationThread()) {
            this.createAndShowDialog(Dialog::show);
            return;
        }



        Platform.runLater(() -> this.createAndShowDialog(Dialog::show));
    }

    public void showAndWait() {
        if (Platform.isFxApplicationThread()) {
            this.createAndShowDialog(Dialog::showAndWait);
            return;
        }



        CountDownLatch countDownLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            this.createAndShowDialog(Dialog::showAndWait);
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {}
    }



    private void createAndShowDialog(Consumer<Dialog<?>> showCommand) {
        Dialog<?> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setOnCloseRequest(_ -> {
            if (autoCloseThread != null) {
                autoCloseThread.interrupt();
            }
        });



        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane
            .getButtonTypes()
            .setAll(ButtonType.OK);
        dialogPane.setMaxWidth(Screen
            .getPrimary()
            .getBounds()
            .getWidth()
            * 0.25
        );



        showCommand.accept(dialog);
        this.scheduleDialogClosing(dialog);
    }

    private void scheduleDialogClosing(Dialog<?> dialog) {
        Runnable closeDialog = () -> {
            try {
                autoCloseTimeUnit.sleep(autoCloseTime);
                Platform.runLater(dialog::close);
            } catch (Exception ignored) {}
        };

        autoCloseThread = Thread
            .ofPlatform()
            .daemon()
            .start(closeDialog);
    }
}
