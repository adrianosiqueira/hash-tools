package hashtools.comparator;

import hashtools.comparator.exception.MissingInputFile1Exception;
import hashtools.comparator.exception.MissingInputFile2Exception;
import hashtools.shared.Extension;
import hashtools.shared.Resource;
import hashtools.shared.TransitionedScreen;
import hashtools.shared.condition.MouseButtonIsPrimaryCondition;
import hashtools.shared.notification.FooterButtonActionNotification;
import hashtools.shared.notification.Notification;
import hashtools.shared.notification.NotificationReceiver;
import hashtools.shared.notification.NotificationSender;
import hashtools.shared.notification.ScreenCloseNotification;
import hashtools.shared.notification.SplashStartNotification;
import hashtools.shared.notification.SplashStopNotification;
import hashtools.shared.operation.ConditionalOperation;
import hashtools.shared.operation.Operation;
import hashtools.shared.operation.SendNotificationOperation;
import hashtools.shared.operation.ShowMessageDialogOperation;
import hashtools.shared.operation.ShowOpenFileDialogOperation;
import hashtools.shared.operation.ShowSaveFileDialogOperation;
import hashtools.shared.operation.StartSplashOperation;
import hashtools.shared.operation.StopSplashOperation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static hashtools.shared.Resource.Software.THREAD_POOL;

public class ComparatorController implements Initializable, NotificationSender, TransitionedScreen {

    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlScreenInput1;
    @FXML
    private Pane pnlScreenInput1Content;
    @FXML
    private Pane pnlScreenInput2;
    @FXML
    private Pane pnlScreenInput2Content;
    @FXML
    private Pane pnlScreenSplash;
    @FXML
    private Pane pnlScreenResult;

    @FXML
    private Labeled lblScreenInput1Header;
    @FXML
    private Labeled lblScreenInput1Content;
    @FXML
    private Labeled lblScreenInput2Header;
    @FXML
    private Labeled lblScreenInput2Content;
    @FXML
    private Labeled lblScreenSplashContent;
    @FXML
    private Labeled lblScreenResultHeader;

    @FXML
    private TextInputControl txtScreenResultContent;


    private Collection<NotificationReceiver> receivers;
    private Collection<Pane> screenPanes;
    private ResourceBundle language;


    @Override
    public Notification getCallerNotification() {
        return new FooterButtonActionNotification(
            new GoToMainScreen(),
            new GoToInputScreen2()
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle language) {
        this.language = language;
        receivers = new ArrayList<>();

        screenPanes = List.of(
            pnlScreenInput1,
            pnlScreenInput2,
            pnlScreenSplash,
            pnlScreenResult
        );

        Operation.perform(
            THREAD_POOL,
            new GoToInputScreen1()
        );
    }

    @FXML
    private void pnlScreenInput1ContentMouseClicked(MouseEvent event) {
        Operation.perform(
            THREAD_POOL,
            new ConditionalOperation(
                new MouseButtonIsPrimaryCondition(event.getButton()),
                new ShowOpenFileDialogOperation(
                    language.getString("hashtools.comparator.comparator-controller.dialog.title.open-file-1"),
                    System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                    Extension.getAllExtensions(language),
                    lblScreenInput1Content,
                    pnlRoot.getScene().getWindow()
                )
            )
        );
    }

    @FXML
    private void pnlScreenInput2ContentMouseClicked(MouseEvent event) {
        Operation.perform(
            THREAD_POOL,
            new ConditionalOperation(
                new MouseButtonIsPrimaryCondition(event.getButton()),
                new ShowOpenFileDialogOperation(
                    language.getString("hashtools.comparator.comparator-controller.dialog.title.open-file-2"),
                    System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                    Extension.getAllExtensions(language),
                    lblScreenInput2Content,
                    pnlRoot.getScene().getWindow()
                )
            )
        );
    }

    @Override
    public void registerNotificationReceiver(NotificationReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void sendNotification(Notification notification) {
        receivers.forEach(receiver -> receiver.receiveNotification(notification));
    }

    @Override
    public void showScreen(Pane screen) {
        screenPanes.forEach(pane -> pane.setVisible(false));
        screen.setVisible(true);
    }


    private final class GoToInputScreen1 extends Operation {
        @Override
        protected void perform() {
            showScreen(pnlScreenInput1);

            sendNotification(
                new FooterButtonActionNotification(
                    new GoToMainScreen(),
                    new GoToInputScreen2()
                )
            );
        }
    }

    private final class GoToInputScreen2 extends Operation {
        @Override
        protected void perform() {
            showScreen(pnlScreenInput2);

            sendNotification(
                new FooterButtonActionNotification(
                    new GoToInputScreen1(),
                    new RunModule()
                )
            );
        }
    }

    private final class GoToMainScreen extends Operation {
        @Override
        protected void perform() {
            sendNotification(new ScreenCloseNotification());
        }
    }

    private final class GoToResultScreen extends Operation {
        @Override
        protected void perform() {
            showScreen(pnlScreenResult);

            sendNotification(
                new FooterButtonActionNotification(
                    new GoToInputScreen2(),
                    new ShowSaveFileDialogOperation(
                        language.getString("hashtools.comparator.comparator-controller.dialog.title.save-file"),
                        System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                        txtScreenResultContent.getText(),
                        pnlRoot.getScene().getWindow()
                    )
                )
            );
        }
    }

    private final class RunModule extends Operation {
        @Override
        protected void perform() {
            Operation.perform(
                THREAD_POOL,
                new StartSplashOperation(pnlRoot),
                new SendNotificationOperation(
                    ComparatorController.this,
                    new SplashStartNotification()
                )
            );

            ComparatorRequest request = new ComparatorRequest();
            request.setInputFile1(Path.of(lblScreenInput1Content.getText()));
            request.setInputFile2(Path.of(lblScreenInput2Content.getText()));

            try {
                ComparatorService service = new ComparatorService();
                ComparatorResponse response = service.processRequest(request);

                String result = service.formatResponse(response, new ComparatorResponseFormatter(language));
                txtScreenResultContent.setText(result);

                Operation.perform(
                    THREAD_POOL,
                    new GoToResultScreen()
                );
            } catch (MissingInputFile1Exception e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        language.getString("hashtools.comparator.comparator-controller.dialog.title.warning"),
                        language.getString("hashtools.comparator.comparator-controller.dialog.content.missing-file-1")
                    ),
                    new GoToInputScreen1()
                );
            } catch (MissingInputFile2Exception e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        language.getString("hashtools.comparator.comparator-controller.dialog.title.warning"),
                        language.getString("hashtools.comparator.comparator-controller.dialog.content.missing-file-2")
                    ),
                    new GoToInputScreen2()
                );
            }

            Operation.perform(
                THREAD_POOL,
                new StopSplashOperation(pnlRoot),
                new SendNotificationOperation(
                    ComparatorController.this,
                    new SplashStopNotification()
                )
            );
        }
    }
}
