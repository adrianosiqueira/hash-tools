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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MessageDialog {

    private String title;
    private String header;
    private String content;

    private int autoCloseTime;
    private TimeUnit autoCloseTimeUnit;



    public MessageDialog() {
        this.withTitle(null);
        this.withHeader(null);
        this.setContent(null);
        this.setAutoClose(0, null);
    }



    public MessageDialog withTitle(String title) {
        this.title = Optional
            .ofNullable(title)
            .orElse("");

        return this;
    }

    public MessageDialog withHeader(String header) {
        this.header = Optional
            .ofNullable(header)
            .orElse("");

        return this;
    }

    public MessageDialog withContent(String message) {
        Optional
            .ofNullable(message)
            .filter(m -> !m.isBlank())
            .ifPresent(this::setContent);

        return this;
    }

    public MessageDialog withContent(ThrowableWrapper throwable) {
        Optional
            .ofNullable(throwable)
            .map(ThrowableWrapper::getStackTrace)
            .ifPresent(this::setContent);

        return this;
    }

    public MessageDialog withContent(Throwable throwable) {
        Optional
            .ofNullable(throwable)
            .map(ThrowableWrapper::new)
            .map(ThrowableWrapper::getStackTrace)
            .ifPresent(this::setContent);

        return this;
    }

    public MessageDialog withAutoClose(int autoCloseTime, TimeUnit autoCloseTimeUnit) {
        if (autoCloseTimeUnit == null) {
            return this;
        }



        this.setAutoClose(autoCloseTime, autoCloseTimeUnit);
        return this;
    }



    private void setContent(String content) {
        this.content = Optional
            .ofNullable(content)
            .orElse("");
    }

    private void setAutoClose(int autoCloseTime, TimeUnit autoCloseTimeUnit) {
        this.autoCloseTime = autoCloseTime > 0
            ? autoCloseTime
            : Integer.MAX_VALUE;

        this.autoCloseTimeUnit = Optional
            .ofNullable(autoCloseTimeUnit)
            .orElse(TimeUnit.DAYS);
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
        this.scheduleDialogClosing(dialog);
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

    private void scheduleDialogClosing(Dialog<?> dialog) {
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
