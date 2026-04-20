package hashtools.view.dialog;

import hashtools.core.model.ThrowableWrapper;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class StackTraceDialog {

    private static final ButtonType[] DEFAULT_BUTTONS_SELECTION = new ButtonType[]{
        ButtonType.OK
    };



    private String title;
    private String message;
    private ThrowableWrapper throwable;
    private ButtonType[] buttons;

    private int autoCloseTime;
    private TimeUnit autoCloseUnit;
    private boolean shouldAutoClose;



    public StackTraceDialog() {
        this.setTitle(null);
        this.setThrowable(null);
        this.setButtons(null);
        this.setAutoclose(0, null);
    }



    public StackTraceDialog setTitle(String title) {
        this.title = Optional
            .ofNullable(title)
            .orElse("");

        return this;
    }

    public StackTraceDialog setMessage(String message) {
        this.message = Optional
            .ofNullable(message)
            .orElse("");

        return this;
    }

    public StackTraceDialog setThrowable(Throwable throwable) {
        this.throwable = new ThrowableWrapper(throwable);
        return this;
    }

    public StackTraceDialog setButtons(ButtonType[] buttons) {
        this.buttons = Optional
            .ofNullable(buttons)
            .map(this::sanitizeButtons)
            .orElse(DEFAULT_BUTTONS_SELECTION);

        return this;
    }

    public StackTraceDialog setAutoclose(int autoCloseTime, TimeUnit autoCloseUnit) {
        if (autoCloseUnit == null) {
            this.shouldAutoClose = false;
            return this;
        }

        this.autoCloseTime = Math.abs(autoCloseTime);
        this.autoCloseUnit = autoCloseUnit;
        this.shouldAutoClose = true;

        return this;
    }



    public void show() {
        this.createAndShowDialog(Dialog::show);
    }

    public void showAndWait() {
        // FIXME It does not wait because the show command is being called in a different thread than the other task.
        this.createAndShowDialog(Dialog::showAndWait);
    }

    private void createAndShowDialog(Consumer<Dialog<Void>> consumer) {
        String headerContent = this.sanitizeHeaderContent(throwable);
        String stackTraceContent = this.getStackTraceContent(throwable);

        TextArea stackTraceArea = new TextArea();
        stackTraceArea.setText(stackTraceContent);
        stackTraceArea.setEditable(false);

        Platform.runLater(() -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setResizable(true);
            dialog.setTitle(title);
            dialog.setHeaderText(headerContent);
            dialog.setContentText(message);



            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setExpandableContent(stackTraceArea);
            dialogPane.setExpanded(false);

            dialogPane
                .getButtonTypes()
                .setAll(buttons);



            consumer.accept(dialog);

            if (shouldAutoClose) {
                this.closeDialogAfterTime(dialog);
            }
        });
    }



    private ButtonType[] sanitizeButtons(ButtonType[] buttons) {
        ButtonType[] sanitizedButtons = Stream
            .of(buttons)
            .filter(Objects::nonNull)
            .toArray(ButtonType[]::new);


        return sanitizedButtons.length != 0
            ? sanitizedButtons
            : DEFAULT_BUTTONS_SELECTION;
    }

    private String sanitizeHeaderContent(ThrowableWrapper throwable) {
        Function<ThrowableWrapper, String> formattingFunction = wrapper -> String.format(
            "%s: %s",
            wrapper.getSimpleClassName(),
            wrapper.getMessage()
        );



        return Optional
            .ofNullable(throwable)
            .map(formattingFunction)
            .orElse("");
    }

    private String getStackTraceContent(ThrowableWrapper throwable) {
        return Optional
            .ofNullable(throwable)
            .map(ThrowableWrapper::getStackTrace)
            .orElse("");
    }

    private void closeDialogAfterTime(Dialog<?> dialog) {
        Runnable autoCloseTask = () -> {
            try {
                autoCloseUnit.sleep(autoCloseTime);
                Platform.runLater(dialog::close);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread autoCloseThread = new Thread(autoCloseTask);
        autoCloseThread.setDaemon(true);
        autoCloseThread.start();
    }
}
