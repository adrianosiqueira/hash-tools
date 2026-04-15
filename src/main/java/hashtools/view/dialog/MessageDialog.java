package hashtools.view.dialog;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MessageDialog {

    private static final ButtonType[] DEFAULT_BUTTONS_SELECTION = new ButtonType[]{
        ButtonType.OK
    };



    private String title;
    private String message;
    private ButtonType[] buttons;

    private int autoCloseTime;
    private TimeUnit autoCloseUnit;
    private boolean shouldAutoClose;



    public MessageDialog() {
        this.setTitle(null);
        this.setMessage(null);
        this.setButtons(null);
        this.setAutoclose(0, null);
    }



    public MessageDialog setTitle(String title) {
        this.title = Optional
            .ofNullable(title)
            .orElse("");

        return this;
    }

    public MessageDialog setMessage(String message) {
        this.message = Optional
            .ofNullable(message)
            .orElse("");

        return this;
    }

    public MessageDialog setButtons(ButtonType[] buttons) {
        this.buttons = Optional
            .ofNullable(buttons)
            .map(this::sanitizeButtons)
            .orElse(DEFAULT_BUTTONS_SELECTION);

        return this;
    }

    public MessageDialog setAutoclose(int autoCloseTime, TimeUnit autoCloseUnit) {
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
        Platform.runLater(() -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setContentText(message);

            dialog
                .getDialogPane()
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
