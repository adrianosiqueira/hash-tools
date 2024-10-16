package hashtools.operation;

import hashtools.notification.Notification;
import hashtools.notification.NotificationReceiver;
import hashtools.notification.NotificationSender;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class OpenScreen implements Operation {

    private final NotificationReceiver receiver;
    private final String fxmlPath;
    private final Pane pnlContent;

    @Override
    public void perform() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));

            Pane pane = loader.load();
            pnlContent.getChildren().setAll(pane);

            NotificationSender sender = loader.getController();
            sender.registerNotificationReceiver(receiver);

            Notification notification = sender.getCallerNotification();
            receiver.receiveNotification(notification);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open the screen: '" + fxmlPath + "'", e);
        }
    }
}