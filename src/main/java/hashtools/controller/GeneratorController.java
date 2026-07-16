package hashtools.controller;

import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileDialog;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.backend.core.strategy.threadpool.ThreadPool;
import hashtools.strategy.algorithmsource.CheckBoxAlgorithmSource;
import hashtools.strategy.inputsource.FileInputSource;
import hashtools.strategy.inputsource.TextInputSource;
import hashtools.backend.core.strategy.threadpool.UnlimitedCoreDaemonThreadPool;
import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationParameter;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.service.GeneratorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
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

public class GeneratorController implements Controller {

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
    private ThreadPool lightTaskThreadPool;



    @Override
    public void close() {
        lightTaskThreadPool.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generatorService = new GeneratorService();
        this.lightTaskThreadPool = new UnlimitedCoreDaemonThreadPool();
    }



    @FXML
    private void performChecksumGeneration() {
        lightTaskThreadPool.run(() -> {
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
            ? new FileInputSource(txtInput.getText())
            : new TextInputSource(txtInput.getText());
    }

    private AlgorithmSource createAlgorithmSource() {
        return new CheckBoxAlgorithmSource(pnlAlgorithm);
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



        try {
            String content = result.formatForSaving();
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
