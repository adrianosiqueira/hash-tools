package hashtools.view.dialog;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StackTraceDialog {

    private static final ButtonType[] DEFAULT_BUTTONS_SELECTION = new ButtonType[]{
        ButtonType.OK
    };



    private String title;
    private Throwable throwable;
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

    public StackTraceDialog setThrowable(Throwable throwable) {
        this.throwable = throwable;
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
        String headerContent = this.sanitizeHeaderContent(this.throwable);
        String stackTraceContent = this.getStackTraceContent(this.throwable);

        TextArea stackTraceArea = new TextArea();
        stackTraceArea.setText(stackTraceContent);
        stackTraceArea.setEditable(false);

        Platform.runLater(() -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText(headerContent);



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

    private String sanitizeHeaderContent(Throwable throwable) {
        if (throwable == null) {
            return "";
        }



        String className = throwable
            .getClass()
            .getSimpleName();

        String message = Optional
            .ofNullable(throwable.getMessage())
            .orElse("");



        return String.format(
            "%s: %s",
            className,
            message
        );
    }

    private String getStackTraceContent(Throwable throwable) {
        if (throwable == null) {
            return "";
        }


        StringWriter stackTraceContent = new StringWriter();

        PrintWriter printWriter = new PrintWriter(stackTraceContent);
        throwable.printStackTrace(printWriter);

        return stackTraceContent.toString();
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
