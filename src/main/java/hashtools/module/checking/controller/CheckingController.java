package hashtools.module.checking.controller;

import hashtools.Main;
import hashtools.core.event.ExceptionThrownEvent;
import hashtools.core.event.HashToolsEventBus;
import hashtools.core.event.HashToolsEventListener;
import hashtools.core.strategy.checksumextractor.ChecksumExtractor;
import hashtools.core.strategy.checksumextractor.FileChecksumExtractor;
import hashtools.core.strategy.checksumextractor.StringChecksumExtractor;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.FileChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.StringChecksumIdentifier;
import hashtools.core.strategy.messagedigest.FileMessageDigestUpdater;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;
import hashtools.core.strategy.messagedigest.StringMessageDigestUpdater;
import hashtools.module.checking.event.CheckingRequestedEvent;
import hashtools.module.checking.event.CheckingResultFormattedEvent;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.service.CheckingService;
import hashtools.view.dialog.FileDialog;
import hashtools.view.dialog.FileExtension;
import hashtools.view.dialog.MessageDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckingController implements Closeable, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckingController.class);



    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlForm;



    @FXML
    private Label lblInput;
    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;
    @FXML
    private Button btnInput;

    @FXML
    private Label lblChecksum;
    @FXML
    private TextField txtChecksum;
    @FXML
    private CheckBox chkChecksum;
    @FXML
    private Button btnChecksum;

    @FXML
    private Pane pnlFormButtons;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnCheck;



    @FXML
    private Pane pnlResult;
    @FXML
    private TextArea txtResult;

    @FXML
    private Pane pnlResultButtons;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnSave;



    private HashToolsEventBus eventBus;
    private HashToolsEventListener<CheckingResultFormattedEvent> formattedResultListener;
    private HashToolsEventListener<ExceptionThrownEvent> exceptionListener;

    private CheckingService checkingService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formattedResultListener = this::showResultScreen;
        exceptionListener = this::showResultScreen;

        eventBus = Main.getEventBus();
        eventBus.register(CheckingResultFormattedEvent.class, formattedResultListener);
        eventBus.register(ExceptionThrownEvent.class, exceptionListener);

        checkingService = new CheckingService(eventBus);



        btnInput
            .disableProperty()
            .bind(chkInput.selectedProperty().not());

        btnChecksum
            .disableProperty()
            .bind(chkChecksum.selectedProperty().not());
    }

    @Override
    public void close() {
        eventBus.unregister(CheckingResultFormattedEvent.class, formattedResultListener);
        eventBus.unregister(ExceptionThrownEvent.class, exceptionListener);

        checkingService.close();
    }



    @FXML
    private void openInputFile() {
        new FileDialog()
            .setTitle("Select the input file")
            .showOpenDialog(null)
            .map(Path::toString)
            .ifPresent(path -> {
                txtInput.setText(path);
                LOGGER.debug("Selected '{}' as the input file.", path);
            });
    }

    @FXML
    private void openChecksumFile() {
        new FileDialog()
            .setTitle("Select the checksums file")
            .setSelectedExtension(FileExtension.HASH)
            .showOpenDialog(null)
            .map(Path::toString)
            .ifPresent(path -> {
                txtChecksum.setText(path);
                LOGGER.debug("Selected '{}' as the checksum file.", path);
            });
    }

    @FXML
    private void clearScreen() {
        txtInput.clear();
        txtChecksum.clear();

        chkInput.setSelected(true);
        chkChecksum.setSelected(true);

        txtResult.clear();
    }

    @FXML
    private void performChecksumChecking() {
        MessageDigestUpdater updater = chkInput.isSelected()
            ? new FileMessageDigestUpdater(txtInput.getText())
            : new StringMessageDigestUpdater(txtInput.getText());

        ChecksumIdentifier identifier = chkInput.isSelected()
            ? new FileChecksumIdentifier(txtInput.getText())
            : new StringChecksumIdentifier(txtInput.getText());

        ChecksumExtractor extractor = chkChecksum.isSelected()
            ? new FileChecksumExtractor(txtChecksum.getText())
            : new StringChecksumExtractor(txtChecksum.getText());

        LOGGER.debug("Using the '{}' as the MessageDigestUpdater.", updater);
        LOGGER.debug("Using the '{}' as the ChecksumIdentifier.", identifier);
        LOGGER.debug("Using the '{}' as the ChecksumExtractor.", extractor);

        CheckingContext context = new CheckingContext();
        context.setUpdater(updater);
        context.setIdentifier(identifier);
        context.setExtractor(extractor);

        eventBus.publish(new CheckingRequestedEvent(context));
    }

    @FXML
    private void closeResultScreen() {
        pnlForm.setVisible(true);
        pnlResult.setVisible(false);

        this.clearScreen();
    }

    private void showResultScreen(CheckingResultFormattedEvent event) {
        String content = event.getContent();
        this.showResultScreen(content);
    }

    private void showResultScreen(ExceptionThrownEvent event) {
        String content = event.getStackTrace();
        this.showResultScreen(content);
    }

    private void showResultScreen(String content) {
        pnlForm.setVisible(false);
        pnlResult.setVisible(true);

        txtResult.setText(content);
    }

    @FXML
    private void saveResult() {
        Optional<Path> selectedFile = new FileDialog()
            .setTitle("Select where to save")
            .showSaveDialog(null);

        if (selectedFile.isEmpty()) {
            return;
        }



        Path destination = selectedFile.get();
        String content = txtResult.getText();

        StandardOpenOption[] options = {
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        };



        try {
            Files.writeString(
                destination,
                content,
                options
            );

            new MessageDialog()
                .setTitle("Checksum Checking")
                .setMessage("Results saved in: " + destination)
                .show();

            LOGGER.info("Results saved to '{}'.", destination);
        } catch (Exception e) {
            LOGGER.error("Failed to save the results to '{}'.", destination, e);
            eventBus.publish(new ExceptionThrownEvent(e));
        }
    }
}
