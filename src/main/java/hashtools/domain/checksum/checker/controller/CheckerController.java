package hashtools.domain.checksum.checker.controller;

import hashtools.core.problem.Problem;
import hashtools.core.source.checksum.ChecksumSource;
import hashtools.core.source.checksum.FileChecksumSource;
import hashtools.core.source.checksum.StringChecksumSource;
import hashtools.core.source.input.FileInputSource;
import hashtools.core.source.input.InputSource;
import hashtools.core.source.input.StringInputSource;
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
import java.util.function.Consumer;

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
        Consumer<Problem> problemDialog = problem -> new MessageDialog()
            .withTitle("Checksum Checking")
            .withHeader("The following problem was found")
            .withContent(problem)
            .show();



        InputSource inputSource = this.createInputSource();
        Optional<Problem> inputSourceProblem = inputSource.checkForProblem();

        if (inputSourceProblem.isPresent()) {
            problemDialog.accept(inputSourceProblem.get());
            return;
        }



        ChecksumSource checksumSource = this.createChecksumSource();
        Optional<Problem> checksumSourceProblem = checksumSource.checkForProblem();

        if (checksumSourceProblem.isPresent()) {
            problemDialog.accept(checksumSourceProblem.get());
            return;
        }



        try {
            String formattedResult = checkerService
                .performChecksumChecking(inputSource, checksumSource)
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



        try {
            Files.writeString(
                selectedFile.get(),
                txtResult.getText(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            new MessageDialog()
                .withTitle("Checksum Checking")
                .withContent("Results saved in: " + selectedFile.get())
                .show();

            LOGGER.info("Results saved to '{}'.", selectedFile.get());
        } catch (Exception e) {
            new MessageDialog()
                .withTitle("Checking Result Saving")
                .withHeader("Failed to save the results into: " + selectedFile.get())
                .withContent(e)
                .show();

            LOGGER.error("Failed to save the results to '{}'.", selectedFile.get(), e);
        }
    }



    private void showResultScreen(String content) {
        pnlForm.setVisible(false);
        pnlResult.setVisible(true);

        txtResult.setText(content);
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
