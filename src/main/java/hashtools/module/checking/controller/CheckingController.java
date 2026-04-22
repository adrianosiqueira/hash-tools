package hashtools.module.checking.controller;

import hashtools.core.strategy.checksumextractor.ChecksumExtractor;
import hashtools.core.strategy.checksumextractor.FileChecksumExtractor;
import hashtools.core.strategy.checksumextractor.StringChecksumExtractor;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.FileChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.StringChecksumIdentifier;
import hashtools.core.strategy.messagedigest.FileMessageDigestUpdater;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;
import hashtools.core.strategy.messagedigest.StringMessageDigestUpdater;
import hashtools.module.checking.api.CheckingAPI;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;
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

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckingController implements Initializable {

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



    private CheckingAPI checkingAPI;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkingAPI = new CheckingAPI();

        btnInput
            .disableProperty()
            .bind(chkInput.selectedProperty().not());

        btnChecksum
            .disableProperty()
            .bind(chkChecksum.selectedProperty().not());
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
        // TODO Perform input validation before proceeding

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



        try {
            LOGGER.info("Performing checksum checking.");
            CheckingResult result = checkingAPI.requestChecksumChecking(context);
            String formattedResult = checkingAPI.requestResultFormatting(result);

            this.showResultScreen(formattedResult);
            LOGGER.info("Checksum checking finished.");
        } catch (Exception e) {
            new MessageDialog()
                .withTitle("Checksum Checking")
                .withHeader("Failed to perform the checksum checking.")
                .withContent(e)
                .show();
            LOGGER.error("Failed to perform checksum checking.", e);
        }
    }

    @FXML
    private void closeResultScreen() {
        pnlForm.setVisible(true);
        pnlResult.setVisible(false);

        this.clearScreen();
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
                .withTitle("Checksum Checking")
                .withContent("Results saved in: " + destination)
                .show();

            LOGGER.info("Results saved to '{}'.", destination);
        } catch (Exception e) {
            new MessageDialog()
                .withTitle("Checking Result Saving")
                .withHeader("Failed to save the results into: " + destination)
                .withContent(e)
                .show();

            LOGGER.error("Failed to save the results to '{}'.", destination, e);
        }
    }
}
