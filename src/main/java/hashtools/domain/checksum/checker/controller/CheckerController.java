package hashtools.domain.checksum.checker.controller;

import hashtools.core.problem.Problem;
import hashtools.core.source.checksum.ChecksumSource;
import hashtools.core.source.checksum.FileChecksumSource;
import hashtools.core.source.checksum.StringChecksumSource;
import hashtools.core.source.input.FileInputSource;
import hashtools.core.source.input.InputSource;
import hashtools.core.source.input.StringInputSource;
import hashtools.domain.checksum.checker.model.CheckerScreenInput;
import hashtools.domain.checksum.checker.service.CheckerService;
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

public class CheckerController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerController.class);



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



    private CheckerService checkerService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkerService = new CheckerService();

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
        CheckerScreenInput screenInput = this.collectScreenInput();
        Optional<Problem> problem = screenInput.identifyProblem();

        if (problem.isPresent()) {
            new MessageDialog()
                .withTitle("Checksum Checking")
                .withHeader("The following problems were found")
                .withContent(problem.get())
                .show();
            return;
        }



        try {
            String formattedResult = checkerService
                .performChecksumChecking(this.createInputSource(), this.createChecksumSource())
                .formatForConsolePrinting();

            this.showResultScreen(formattedResult);
        } catch (Exception e) {
            new MessageDialog()
                .withTitle("Checksum Checking")
                .withHeader("Failed to perform the checksum checking.")
                .withContent(e)
                .show();
        }
    }

    @FXML
    private void closeResultScreen() {
        pnlForm.setVisible(true);
        pnlResult.setVisible(false);

        this.clearScreen();
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



    private void showResultScreen(String content) {
        pnlForm.setVisible(false);
        pnlResult.setVisible(true);

        txtResult.setText(content);
    }

    private CheckerScreenInput collectScreenInput() {
        CheckerScreenInput screenInput = new CheckerScreenInput();
        screenInput.setInput(txtInput.getText());
        screenInput.setUsingInputFile(chkInput.isSelected());
        screenInput.setChecksum(txtChecksum.getText());
        screenInput.setUsingChecksumFile(chkChecksum.isSelected());

        return screenInput;
    }

    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? new FileInputSource(txtInput.getText())
            : new StringInputSource(txtInput.getText());
    }

    private ChecksumSource createChecksumSource() {
        return chkChecksum.isSelected()
            ? new FileChecksumSource(txtChecksum.getText())
            : new StringChecksumSource(txtChecksum.getText());
    }
}
