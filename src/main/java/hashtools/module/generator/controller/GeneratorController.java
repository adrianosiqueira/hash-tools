package hashtools.module.generator.controller;

import hashtools.core.checksum.Algorithm;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.generator.domain.ChecksumGenerationCallback;
import hashtools.module.generator.domain.ChecksumGenerationParameter;
import hashtools.module.generator.domain.ChecksumGenerationResult;
import hashtools.module.generator.service.GeneratorService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GeneratorController implements Initializable {

    @FXML
    private TextField txtInput;
    @FXML
    private CheckBox chkInput;

    @FXML
    private Pane pnlAlgorithm;

    @FXML
    private Button btnExecute;
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
            disableUi();



            // Data retrieval
            InputSource inputSource = this.createInputSource();
            List<Algorithm> algorithms = this.createAlgorithmList();



            // Communication setup
            ChecksumGenerationParameter parameter = new ChecksumGenerationParameter();
            parameter.setInputSource(inputSource);
            parameter.setAlgorithms(algorithms);

            ChecksumGenerationCallback callback = new ChecksumGenerationCallback();
            callback.addProgressConsumer(this::trackProgress);
            callback.addResultConsumer(this::presentResult);
            callback.addProblemConsumer(this::showMessageDialog);
            callback.addExceptionConsumer(this::logException);



            // Processing
            generatorService.performChecksumGeneration(
                parameter,
                callback
            );
        });
    }

    @FXML
    private void openInputFile() {
    }



    private InputSource createInputSource() {
        return chkInput.isSelected()
            ? InputSource.fileInputSource(txtInput.getText())
            : InputSource.textInputSource(txtInput.getText());
    }

    private List<Algorithm> createAlgorithmList() {
        return pnlAlgorithm
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .filter(CheckBox::isSelected)
            .map(CheckBox::getText)
            .map(Algorithm::getByName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }



    private void trackProgress(double progress) {
        prgProgress.setProgress(progress);
    }

    private void presentResult(ChecksumGenerationResult result) {
        enableUi();

        IO.println(result.getIdentification());
        result.getChecksums().forEach(IO::println);
        // Open a dialog to save the result
    }

    private void showMessageDialog(String message) {
        IO.println(message);
        enableUi();
    }

    private void logException(Exception exception) {
        //noinspection CallToPrintStackTrace
        exception.printStackTrace();
        enableUi();
    }



    private void disableUi() {
        btnExecute.setDisable(true);
    }

    private void enableUi() {
        btnExecute.setDisable(false);
    }
}
