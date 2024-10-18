package hashtools.controller;

import hashtools.condition.MouseButtonIsPrimary;
import hashtools.domain.Algorithm;
import hashtools.domain.Extension;
import hashtools.domain.GeneratorRequest;
import hashtools.domain.GeneratorResponse;
import hashtools.domain.Resource;
import hashtools.formatter.CLIGeneratorResponseFormatter;
import hashtools.identification.FileIdentification;
import hashtools.messagedigest.FileUpdater;
import hashtools.notification.FooterButtonActionNotification;
import hashtools.notification.Notification;
import hashtools.notification.NotificationReceiver;
import hashtools.notification.NotificationSender;
import hashtools.operation.ConditionalOperation;
import hashtools.operation.Operation;
import hashtools.operation.OperationPerformer;
import hashtools.service.Service;
import hashtools.util.DialogUtil;
import hashtools.util.FileUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static hashtools.domain.Resource.StaticImplementation.NO_CONDITION;

@Slf4j
public class GeneratorScreenController implements Initializable, NotificationSender, TransitionedScreen {

    @FXML
    private Pane
        pnlRoot,
        pnlScreenInput,
        pnlScreenInputContent,
        pnlScreenAlgorithm,
        pnlScreenAlgorithmContent,
        pnlScreenSplash,
        pnlScreenResult;

    @FXML
    private Labeled
        lblScreenInputHeader,
        lblScreenInputContent,
        lblScreenAlgorithmHeader,
        lblScreenSplashContent,
        lblScreenResultHeader;

    @FXML
    private CheckBox
        chkMd5,
        chkSha1,
        chkSha224,
        chkSha256,
        chkSha384,
        chkSha512;

    @FXML
    private TextInputControl txtScreenResultContent;


    private Collection<NotificationReceiver> receivers;
    private Collection<Pane> screenPanes;
    private ResourceBundle language;


    @Override
    public Notification getCallerNotification() {
        return new FooterButtonActionNotification(
            new ConditionalOperation(NO_CONDITION, new GoToMainScreen()),
            new ConditionalOperation(NO_CONDITION, new GoToAlgorithmScreen())
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle language) {
        this.language = language;
        receivers = new ArrayList<>();

        screenPanes = List.of(
            pnlScreenInput,
            pnlScreenAlgorithm,
            pnlScreenSplash,
            pnlScreenResult
        );

        OperationPerformer.performAsync(new GoToInputScreen());
    }

    @FXML
    private void pnlScreenInputContentMouseClicked(MouseEvent event) {
        OperationPerformer.performAsync(
            new MouseButtonIsPrimary(event),
            new OpenInputFile()
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
    }


    private final class GenerateChecksums implements Operation {
        @Override
        public void perform() {
            Path inputFile = Path.of(lblScreenInputContent.getText());

            List<Algorithm> algorithms = pnlScreenAlgorithmContent
                .getChildren()
                .stream()
                .filter(CheckBox.class::isInstance)
                .map(CheckBox.class::cast)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .map(name -> Algorithm.from(name).orElseThrow())
                .toList();

            GeneratorRequest request = new GeneratorRequest();
            request.setInput(new FileUpdater(inputFile));
            request.setIdentification(new FileIdentification(inputFile));
            request.setAlgorithms(algorithms);

            Service service = new Service();
            GeneratorResponse response = service.run(request);

            String result = service.format(response, new CLIGeneratorResponseFormatter());
            txtScreenResultContent.setText(result);
        }
    }

    private final class GoToAlgorithmScreen implements Operation {
        @Override
        public void perform() {
            showScreen(pnlScreenAlgorithm);

//            btnBackAction = new ConditionalAction(new GoToInputScreen());
//            btnNextAction = new ConditionalAction(new GoToSplashScreen());
        }
    }

    private final class GoToInputScreen implements Operation {
        @Override
        public void perform() {
            showScreen(pnlScreenInput);

//            btnBackAction = new ConditionalAction(new GoToMainScreen());
//            btnNextAction = new ConditionalAction(new GoToAlgorithmScreen());
        }
    }

    private final class GoToMainScreen implements Operation {
        @Override
        public void perform() {
            pnlRoot.setVisible(false);
        }
    }

    private final class GoToResultScreen implements Operation {
        @Override
        public void perform() {
            showScreen(pnlScreenResult);

//            btnBackAction = new ConditionalAction(new GoToAlgorithmScreen());
//            btnNextAction = new ConditionalAction(new SaveResultToFile());
        }
    }

    private final class GoToSplashScreen implements Operation {
        @Override
        public void perform() {
            showScreen(pnlScreenSplash);

            OperationPerformer.performAsync(new StartSplash());
            OperationPerformer.perform(new GenerateChecksums());
            OperationPerformer.performAsync(new StopSplash());
            OperationPerformer.performAsync(new GoToResultScreen());
        }
    }

    private final class OpenInputFile implements Operation {
        @Override
        public void perform() {
            Platform.runLater(() -> DialogUtil
                .showOpenDialog(
                    "Select a file to generate",
                    System.getProperty("user.home"),
                    Extension.getAllExtensions(language),
                    pnlRoot.getScene().getWindow())
                .map(Path::toString)
                .ifPresent(lblScreenInputContent::setText)
            );
        }
    }

    private final class SaveResultToFile implements Operation {
        @Override
        public void perform() {
            Platform.runLater(() -> DialogUtil
                .showSaveDialog(
                    "Choose where to save",
                    System.getProperty("user.home"),
                    pnlRoot.getScene().getWindow())
                .ifPresent(file -> FileUtil.replaceContent(txtScreenResultContent.getText(), file))
            );
        }
    }

    private final class StartSplash implements Operation {
        @Override
        public void perform() {
            // TODO Replace this statement with a css rule
            pnlRoot.setCursor(Cursor.WAIT);
            pnlRoot.pseudoClassStateChanged(Resource.Static.DISABLED, true);
            pnlRoot
                .getChildren()
                .forEach(node -> node.setDisable(true));
        }
    }

    private final class StopSplash implements Operation {
        @Override
        public void perform() {
            // TODO Replace this statement with a css rule
            pnlRoot.setCursor(Cursor.DEFAULT);
            pnlRoot.pseudoClassStateChanged(Resource.Static.DISABLED, false);
            pnlRoot
                .getChildren()
                .forEach(node -> node.setDisable(false));
        }
    }
}
