package hashtools.controller;

import hashtools.core.event.ExceptionThrownEvent;
import hashtools.core.event.HashToolsEventBus;
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
import hashtools.module.comparison.ChecksumComparisonContext;
import hashtools.module.comparison.ChecksumComparisonResult;
import hashtools.module.generation.ChecksumGenerationContext;
import hashtools.module.generation.ChecksumGenerationResult;
import hashtools.service.EventService;
import hashtools.view.dialog.FileDialog;
import hashtools.view.dialog.FileExtension;
import hashtools.view.dialog.MessageDialog;
import hashtools.view.dialog.StackTraceDialog;
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



    private Runnable lastScreenOpeningCommand;
    private HashToolsEventBus eventBus;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus = EventService.INSTANCE;
        eventBus.register(CheckingResultFormattedEvent.class, this::openResultScreen);
        eventBus.register(ExceptionThrownEvent.class, this::handleException);
        eventBus.register(ChecksumComparisonResult.class, this::openResultScreen);
        eventBus.register(ChecksumGenerationResult.class, this::openResultScreen);

        CheckingService.registerListeners(eventBus);

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

    private void openScreen(Pane screen, Runnable lastScreenOpeningCommand) {
        pnlModuleChecker.setVisible(false);
        pnlModuleComparator.setVisible(false);
        pnlModuleGenerator.setVisible(false);
        pnlResult.setVisible(false);

        screen.setVisible(true);
        this.lastScreenOpeningCommand = lastScreenOpeningCommand;

        this.clearScreen();
    }



    private void openResultScreen(CheckingResultFormattedEvent event) {
        new MessageDialog()
            .setTitle("Checksum Checking")
            .setMessage("The checking has been finalized.")
            .show();

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
        new FileDialog()
            .setTitle("Select the input file")
            .showOpenDialog(null)
            .map(Path::toString)
            .ifPresent(txtModuleCheckerInput::setText);
    }

    @FXML
    private void openCheckerOfficialFile() {
        new FileDialog()
            .setTitle("Select the checksums file")
            .setSelectedExtension(FileExtension.HASH)
            .showOpenDialog(null)
            .map(Path::toString)
            .ifPresent(txtModuleCheckerOfficial::setText);
    }

    @FXML
    private void performChecksumChecking() {
        MessageDigestUpdater updater = chkModuleCheckerInputFile.isSelected()
            ? new FileMessageDigestUpdater(txtModuleCheckerInput.getText())
            : new StringMessageDigestUpdater(txtModuleCheckerInput.getText());

        ChecksumIdentifier identifier = chkModuleCheckerInputFile.isSelected()
            ? new FileChecksumIdentifier(txtModuleCheckerInput.getText())
            : new StringChecksumIdentifier(txtModuleCheckerInput.getText());

        ChecksumExtractor extractor = chkModuleCheckerOfficialFile.isSelected()
            ? new FileChecksumExtractor(txtModuleCheckerOfficial.getText())
            : new StringChecksumExtractor(txtModuleCheckerOfficial.getText());

        CheckingContext context = new CheckingContext();
        context.setUpdater(updater);
        context.setIdentifier(identifier);
        context.setExtractor(extractor);

        eventBus.publish(new CheckingRequestedEvent(context));
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
        lastScreenOpeningCommand.run();
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
        } catch (Exception e) {
            eventBus.publish(new ExceptionThrownEvent(e));
        }
    }



    private void handleException(ExceptionThrownEvent event) {
        new StackTraceDialog()
            .setTitle("Application Controller")
            .setThrowable(event.getThrowable())
            .show();
    }
}
