package hashtools.controller;

import hashtools.core.event.HashToolsEventBus;
import hashtools.core.model.Checksum;
import hashtools.module.checking.event.CheckingRequestedEvent;
import hashtools.module.checking.event.CheckingResultFormattedEvent;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.service.CheckingService;
import hashtools.module.comparison.ChecksumComparisonContext;
import hashtools.module.comparison.ChecksumComparisonResult;
import hashtools.module.generation.ChecksumGenerationContext;
import hashtools.module.generation.ChecksumGenerationResult;
import hashtools.service.EventService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class ApplicationController implements Initializable {

    @FXML private Pane pnlRoot;
    @FXML private Pane pnlContent;



    @FXML private Pane pnlModuleChecker;
    @FXML private Button btnModuleChecker;
    @FXML private Label lblModuleCheckerInput;
    @FXML private TextField txtModuleCheckerInput;
    @FXML private CheckBox chkModuleCheckerInputFile;
    @FXML private Button btnModuleCheckerOpenFile;
    @FXML private Label lblModuleCheckerOfficial;
    @FXML private TextField txtModuleCheckerOfficial;
    @FXML private CheckBox chkModuleCheckerOfficialFile;
    @FXML private Button btnModuleCheckerOpenOfficial;
    @FXML private Pane pnlModuleCheckerButtons;
    @FXML private Button btnModuleCheckerClear;
    @FXML private Button btnModuleCheckerCheck;



    @FXML private Pane pnlModuleGenerator;
    @FXML private Button btnModuleGenerator;



    @FXML private Pane pnlModuleComparator;
    @FXML private Button btnModuleComparator;



    @FXML private Pane pnlResult;
    @FXML private TextArea txtResult;
    @FXML private Pane pnlResultButtons;
    @FXML private Button btnResultBack;
    @FXML private Button btnResultSave;



    private Runnable lastOpenedScreen;
    private HashToolsEventBus eventBus;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus = EventService.INSTANCE;
        eventBus.register(CheckingResultFormattedEvent.class, this::openResultScreen);
        eventBus.register(ChecksumComparisonResult.class, this::openResultScreen);
        eventBus.register(ChecksumGenerationResult.class, this::openResultScreen);

        new CheckingService(eventBus);

        btnModuleCheckerOpenFile
            .disableProperty()
            .bind(chkModuleCheckerInputFile.selectedProperty().not());

        btnModuleCheckerOpenOfficial
            .disableProperty()
            .bind(chkModuleCheckerOfficialFile.selectedProperty().not());

        this.openCheckerScreen();
    }



    @FXML
    private void openCheckerScreen() {
        this.openScreen(pnlModuleChecker, this::openCheckerScreen);
    }

    @FXML
    private void openComparatorScreen() {
        this.openScreen(pnlModuleComparator, this::openComparatorScreen);
    }

    @FXML
    private void openGeneratorScreen() {
        this.openScreen(pnlModuleGenerator, this::openGeneratorScreen);
    }

    private void openScreen(Pane screen, Runnable lastOpenedScreen) {
        pnlModuleChecker.setVisible(false);
        pnlModuleComparator.setVisible(false);
        pnlModuleGenerator.setVisible(false);
        pnlResult.setVisible(false);

        screen.setVisible(true);
        this.lastOpenedScreen = lastOpenedScreen;

        this.clearScreen();
    }



    private void openResultScreen(CheckingResultFormattedEvent event) {
        this.openResultScreen(event::getContent);
    }

    private void openResultScreen(ChecksumComparisonResult result) {
        // TODO Implement a class to get a string representation of the ChecksumComparisonResult
        this.openResultScreen(result::toString);
    }

    private void openResultScreen(ChecksumGenerationResult result) {
        // TODO Implement a class to get a string representation of the ChecksumGenerationResult
        this.openResultScreen(result::toString);
    }

    private void openResultScreen(Supplier<String> resultSupplier) {
        pnlModuleChecker.setVisible(false);
        pnlModuleComparator.setVisible(false);
        pnlModuleGenerator.setVisible(false);
        pnlResult.setVisible(true);

        txtResult.setText(resultSupplier.get());
    }



    @FXML
    private void openCheckerInputFile() {
        // TODO Get the file from a dialog window
        Optional
            .<Path>ofNullable(null)
            .map(Path::toAbsolutePath)
            .map(Path::toString)
            .ifPresent(txtModuleCheckerInput::setText);
    }

    @FXML
    private void openCheckerOfficialFile() {
        // TODO Get the file from a dialog window
        Optional
            .<Path>ofNullable(null)
            .map(Path::toAbsolutePath)
            .map(Path::toString)
            .ifPresent(txtModuleCheckerOfficial::setText);
    }

    @FXML
    private void performChecksumChecking() {
        // TODO Fill request event
        ChecksumCheckingRequestedEvent event = new ChecksumCheckingRequestedEvent();
        eventBus.publish(event);
    }



    @FXML
    private void performChecksumComparison() {
        // TODO Fill the context
        ChecksumComparisonContext context = new ChecksumComparisonContext();
        eventBus.publish(context);
    }



    @FXML
    private void performChecksumGeneration() {
        // TODO Fill the context
        ChecksumGenerationContext context = new ChecksumGenerationContext();
        eventBus.publish(context);
    }



    @FXML
    private void clearScreen() {
        txtModuleCheckerInput.clear();
        txtModuleCheckerOfficial.clear();

        chkModuleCheckerInputFile.setSelected(true);
        chkModuleCheckerOfficialFile.setSelected(true);

        txtResult.clear();
    }

    @FXML
    private void closeResultScreen() {
        lastOpenedScreen.run();
    }

    @FXML
    private void saveResult() {
        // TODO Get a path from a dialog box
        Path destination = null;
        String content = txtResult.getText();

        StandardOpenOption[] options = {
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        };

        try {
            Files.writeString(destination, content, options);
            // TODO Display a success message in a dialog box
        } catch (Exception e) {
            // TODO Display the exception in a dialog box
            e.printStackTrace();
        }
    }
}
