package hashtools.view.dialog;

import hashtools.core.model.ThrowableWrapper;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageDialog {

    private String title;
    private String header;
    private String content;

    private int autoCloseTime;
    private TimeUnit autoCloseTimeUnit;
    private boolean shouldAutoClose;



    public MessageDialog() {
        this.setTitle(null);
        this.setHeader(null);
        this.setContent(null);
        this.setAutoClose(0, null);
    }



    public MessageDialog setTitle(String title) {
        this.title = Optional
            .ofNullable(title)
            .orElse("");

        return this;
    }

    public MessageDialog setHeader(String header) {
        this.header = Optional
            .ofNullable(header)
            .orElse("");

        return this;
    }

    public MessageDialog setMessage(String message) {
        Optional
            .ofNullable(message)
            .filter(m -> !m.isBlank())
            .ifPresent(this::setContent);

        return this;
    }

    public MessageDialog setThrowable(ThrowableWrapper throwable) {
        Optional
            .ofNullable(throwable)
            .map(ThrowableWrapper::getStackTrace)
            .ifPresent(this::setContent);

        return this;
    }

    public MessageDialog setThrowable(Throwable throwable) {
        Optional
            .ofNullable(throwable)
            .map(ThrowableWrapper::new)
            .ifPresent(this::setThrowable);

        return this;
    }

    public MessageDialog setAutoClose(int autoCloseTime, TimeUnit autoCloseTimeUnit) {
        this.autoCloseTime = autoCloseTime;
        this.autoCloseTimeUnit = autoCloseTimeUnit;
        this.shouldAutoClose = autoCloseTimeUnit != null;

        return this;
    }

    private void setContent(String content) {
        this.content = Optional
            .ofNullable(content)
            .orElse("");
    }



    public void show() {
        Platform.runLater(() -> this.createAndShowDialog(Dialog::show));
    }



    private void createAndShowDialog(Consumer<Dialog<?>> showCommand) {
        Dialog<?> dialog = new Dialog<>();
        dialog.setResizable(true);
        dialog.setTitle(title);
        dialog.setHeaderText(header);



        String style = """
            -fx-background-color: white;
            -fx-border-color: transparent;
            -fx-border-width: 0;
            """;

        TextArea txtContent = new TextArea();
        txtContent.setEditable(false);
        txtContent.setText(content);
        txtContent.setStyle(style);

        dialog
            .getDialogPane()
            .setContent(txtContent);



        dialog
            .getDialogPane()
            .getButtonTypes()
            .setAll(ButtonType.OK);



        Dimension2D dimension = this.calculateDimension();
        dialog.setWidth(dimension.getWidth());
        dialog.setHeight(dimension.getHeight());



        showCommand.accept(dialog);



        if (shouldAutoClose) {
            Runnable closeDialog = () -> {
                try {
                    autoCloseTimeUnit.sleep(autoCloseTime);
                    Platform.runLater(dialog::close);
                } catch (Exception ignored) {}
            };

            Thread
                .ofPlatform()
                .daemon()
                .start(closeDialog);
        }
    }

    private Dimension2D calculateDimension() {
        Rectangle2D screen = Screen
            .getPrimary()
            .getBounds();

        double width = screen
            .getWidth()
            * 0.25;

        double height = screen
            .getHeight()
            * 0.25;



        return new Dimension2D(
            width,
            height
        );
    }
}
