package hashtools.module.generator.controller;

import hashtools.core.checksum.Checksum;
import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileDialog;
import hashtools.core.source.AlgorithmSource;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.generator.domain.ChecksumGenerationContainer;
import hashtools.module.generator.domain.ChecksumGenerationParameter;
import hashtools.module.generator.domain.ChecksumGenerationResult;
import hashtools.module.generator.service.GeneratorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GeneratorController implements Initializable {

    @FXML
    private Pane pnlRoot;

    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;

    @FXML
    private Pane pnlAlgorithm;

    @FXML
    private ProgressBar prgProgress;

    private GeneratorService generatorService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generatorService = new GeneratorService();
    }



    @FXML
    private void performChecksumGeneration() {
        ThreadPool.CACHED_DAEMON.execute(() -> {
            // User feedback
            disableUi(pnlRoot);



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            AlgorithmSource algorithmSource = this.createAlgorithmSource();



            // Communication setup
            ChecksumGenerationParameter parameter = new ChecksumGenerationParameter();
            parameter.setInputSource(inputSource);
            parameter.setAlgorithmSource(algorithmSource);
            parameter.setProgressConsumer(this::trackProgress);



            // Processing
            ChecksumGenerationContainer container = generatorService.performChecksumGeneration(parameter);
            container.consumeResultIfPresent(this::saveResult);
            container.consumeProblemIfPresent(this::showMessageDialog);
            container.consumeExceptionIfPresent(this::logException);
        });
    }

    @FXML
    private void openInputFile() {
        new FileDialog()
            .withTitle("Select the input file")
            .openForReading()
            .map(EnhancedFile::toString)
            .ifPresent(txtInput::setText);
    }



    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? InputSource.fileInputSource(txtInput.getText())
            : InputSource.textInputSource(txtInput.getText());
    }

    private AlgorithmSource createAlgorithmSource() {
        return AlgorithmSource.checkBoxAlgorithmSource(pnlAlgorithm);
    }



    private void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }

    private void saveResult(ChecksumGenerationResult result) {
        enableUi(pnlRoot);

        EnhancedFile file = new FileDialog()
            .withTitle("Select where to save the checksums")
            .openForWriting()
            .orElse(null);

        if (file == null) {
            return;
        }

        String content = result
            .getChecksums()
            .stream()
            .map(Checksum::toString)
            .collect(Collectors.joining("\n"));

        try {
            file.replaceContent(content);
        } catch (IOException e) {
            logException(e);
        }
    }

    private void showMessageDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hash Tools");
            alert.setHeaderText("Problem");
            alert.setContentText(message);
            alert.show();
        });

        enableUi(pnlRoot);
    }

    private void logException(Exception exception) {
        //noinspection CallToPrintStackTrace
        exception.printStackTrace();
        enableUi(pnlRoot);
    }



    private void disableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.WAIT);
            pane.getChildren().forEach(this::disableUi);
        } else if (node instanceof Control control) {
            control.setDisable(true);
        }
    }

    private void enableUi(Node node) {
        if (node instanceof Pane pane) {
            pane.setCursor(Cursor.DEFAULT);
            pane.getChildren().forEach(this::enableUi);
        } else if (node instanceof Control control) {
            control.setDisable(false);
        }
    }
}
