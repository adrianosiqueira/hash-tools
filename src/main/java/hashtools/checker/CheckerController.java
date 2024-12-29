package hashtools.checker;

import hashtools.checker.exception.InvalidChecksumFileSizeException;
import hashtools.checker.exception.InvalidChecksumFileTypeException;
import hashtools.checker.exception.MissingChecksumFileException;
import hashtools.checker.exception.MissingInputFileException;
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

public class CheckerController implements Initializable, NotificationSender, TransitionedScreen {

    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlScreenInput;
    @FXML
    private Pane pnlScreenInputContent;
    @FXML
    private Pane pnlScreenChecksum;
    @FXML
    private Pane pnlScreenChecksumContent;
    @FXML
    private Pane pnlScreenSplash;
    @FXML
    private Pane pnlScreenResult;

    @FXML
    private Labeled lblScreenInputHeader;
    @FXML
    private Labeled lblScreenInputContent;
    @FXML
    private Labeled lblScreenChecksumHeader;
    @FXML
    private Labeled lblScreenChecksumContent;
    @FXML
    private Labeled lblScreenSplashContent;
    @FXML
    private Labeled lblScreenResultHeader;

    @FXML
    private TextInputControl txtResult;


    private Collection<NotificationReceiver> receivers;
    private Collection<Pane> screenPanes;
    private ResourceBundle checkerLanguage;
    private ResourceBundle sharedLanguage;


    @Override
    public Notification getCallerNotification() {
        return new FooterButtonActionNotification(
            new GoToMainScreen(),
            new GoToChecksumScreen()
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle language) {
        checkerLanguage = language;
        sharedLanguage = ResourceBundle.getBundle(Resource.Language.SHARED);
        receivers = new ArrayList<>();

        screenPanes = List.of(
            pnlScreenInput,
            pnlScreenChecksum,
            pnlScreenSplash,
            pnlScreenResult
        );

        Operation.perform(
            THREAD_POOL,
            new GoToInputScreen()
        );
    }

    @FXML
    private void pnlScreenChecksumContentMouseClicked(MouseEvent event) {
        Operation.perform(
            THREAD_POOL,
            new ConditionalOperation(
                new MouseButtonIsPrimaryCondition(event.getButton()),
                new ShowOpenFileDialogOperation(
                    checkerLanguage.getString("checker-controller.dialog.title.open-checksum"),
                    System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                    List.of(
                        Extension.HASH.getFilter(sharedLanguage),
                        Extension.ALL.getFilter(sharedLanguage)
                    ),
                    lblScreenChecksumContent,
                    pnlRoot.getScene().getWindow()
                )
            )
        );
    }

    @FXML
    private void pnlScreenInputContentMouseClicked(MouseEvent event) {
        Operation.perform(
            THREAD_POOL,
            new ConditionalOperation(
                new MouseButtonIsPrimaryCondition(event.getButton()),
                new ShowOpenFileDialogOperation(
                    checkerLanguage.getString("checker-controller.dialog.title.open-file"),
                    System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                    Extension.getAllExtensions(sharedLanguage),
                    lblScreenInputContent,
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


    private class GoToChecksumScreen extends Operation {
        @Override
        protected void perform() {
            showScreen(pnlScreenChecksum);

            sendNotification(
                new FooterButtonActionNotification(
                    new GoToInputScreen(),
                    new RunModule()
                )
            );
        }
    }

    private final class GoToInputScreen extends Operation {
        @Override
        protected void perform() {
            showScreen(pnlScreenInput);

            sendNotification(
                new FooterButtonActionNotification(
                    new GoToMainScreen(),
                    new GoToChecksumScreen()
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
                    new GoToChecksumScreen(),
                    new ShowSaveFileDialogOperation(
                        checkerLanguage.getString("checker-controller.dialog.title.save-file"),
                        System.getProperty(Resource.PropertyKey.HOME_DIRECTORY),
                        txtResult.getText(),
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
                    CheckerController.this,
                    new SplashStartNotification()
                )
            );

            CheckerRequest request = new CheckerRequest();
            request.setInputFile(Path.of(lblScreenInputContent.getText()));
            request.setChecksumFile(Path.of(lblScreenChecksumContent.getText()));

            try {
                CheckerService service = new CheckerService(checkerLanguage);
                CheckerResponse response = service.processRequest(request);

                String result = service.formatResponse(response);
                txtResult.setText(result);

                Operation.perform(
                    THREAD_POOL,
                    new GoToResultScreen()
                );
            } catch (MissingInputFileException e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        checkerLanguage.getString("checker-controller.dialog.title.warning"),
                        checkerLanguage.getString("checker-controller.dialog.content.missing-file")
                    ),
                    new GoToInputScreen()
                );
            } catch (MissingChecksumFileException e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        checkerLanguage.getString("checker-controller.dialog.title.warning"),
                        checkerLanguage.getString("checker-controller.dialog.content.missing-checksum")
                    ),
                    new GoToChecksumScreen()
                );
            } catch (InvalidChecksumFileTypeException e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        checkerLanguage.getString("checker-controller.dialog.title.warning"),
                        checkerLanguage.getString("checker-controller.dialog.content.checksum-not-text")
                    ),
                    new GoToChecksumScreen()
                );
            } catch (InvalidChecksumFileSizeException e) {
                Operation.perform(
                    THREAD_POOL,
                    new ShowMessageDialogOperation(
                        checkerLanguage.getString("checker-controller.dialog.title.warning"),
                        checkerLanguage.getString("checker-controller.dialog.content.checksum-too-big")
                    ),
                    new GoToChecksumScreen()
                );
            }

            Operation.perform(
                THREAD_POOL,
                new StopSplashOperation(pnlRoot),
                new SendNotificationOperation(
                    CheckerController.this,
                    new SplashStopNotification()
                )
            );
        }
    }
}
