package hash_tools.frontend.screen.start;

import hash_tools.frontend.abstraction.ClosingObservable;
import hash_tools.frontend.window.ApplicationWindow;
import hash_tools.frontend.fxml.FXMLFile;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private static final String CHECKER_SCREEN_PATH = "/hash_tools/frontend/screen/checker/main-screen.fxml";
    private static final String COMPARATOR_SCREEN_PATH = "/hash_tools/frontend/screen/comparator_screen/main-screen.fxml";
    private static final String GENERATOR_SCREEN_PATH = "/hash_tools/frontend/screen/generator_screen/main-screen.fxml";



    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlChecker;
    @FXML
    private Pane pnlComparator;
    @FXML
    private Pane pnlGenerator;

    @FXML
    private Label lblCheckerTitle;
    @FXML
    private Label lblCheckerDescription;
    @FXML
    private Label lblComparatorTitle;
    @FXML
    private Label lblComparatorDescription;
    @FXML
    private Label lblGeneratorTitle;
    @FXML
    private Label lblGeneratorDescription;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlChecker.setOnMouseClicked(this::openCheckerScreen);
        pnlComparator.setOnMouseClicked(this::openComparatorScreen);
        pnlGenerator.setOnMouseClicked(this::openGeneratorScreen);
    }



    private void openCheckerScreen(Event unused) {
        new FXMLFile()
            .location(CHECKER_SCREEN_PATH)
            .<ClosingObservable>load()
            .usePane(ApplicationWindow::changeScene)
            .useController(c -> c.performWhenClosed(this::restoreDefaultScreen));
    }

    private void openComparatorScreen(Event unused) {
        new FXMLFile()
            .location(COMPARATOR_SCREEN_PATH)
            .<ClosingObservable>load()
            .usePane(ApplicationWindow::changeScene)
            .useController(c -> c.performWhenClosed(this::restoreDefaultScreen));
    }

    private void openGeneratorScreen(Event unused) {
        new FXMLFile()
            .location(GENERATOR_SCREEN_PATH)
            .<ClosingObservable>load()
            .usePane(ApplicationWindow::changeScene)
            .useController(c -> c.performWhenClosed(this::restoreDefaultScreen));
    }



    private void restoreDefaultScreen() {
        ApplicationWindow.changeScene(pnlRoot);
    }
}
