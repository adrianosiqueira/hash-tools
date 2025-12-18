package hash_tools._interface.main_screen;

import hash_tools._interface.abstraction.ClosingObservable;
import hash_tools._interface.checker_screen.CheckerScreenController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private static final String CHECKER_SCREEN_PATH = "/hash_tools/_interface/checker_screen/checker-screen.fxml";
    private static final String COMPARATOR_SCREEN_PATH = "/hash_tools/_interface/comparator_screen/comparator-screen.fxml";
    private static final String GENERATOR_SCREEN_PATH = "/hash_tools/_interface/generator_screen/generator-screen.fxml";



    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlContent;
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
        FXMLData<CheckerScreenController> data = loadFXML(CHECKER_SCREEN_PATH);
        setupScreen(data);
    }

    private void openComparatorScreen(Event unused) {
        // TODO Use the correct controller type
        FXMLData<CheckerScreenController> data = loadFXML(COMPARATOR_SCREEN_PATH);
        setupScreen(data);
    }

    private void openGeneratorScreen(Event unused) {
        // TODO Use the correct controller type
        FXMLData<CheckerScreenController> data = loadFXML(GENERATOR_SCREEN_PATH);
        setupScreen(data);
    }



    private void restoreDefaultScreen() {
        pnlRoot
            .getChildren()
            .setAll(pnlContent);
    }

    private <C extends ClosingObservable> FXMLData<C> loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));

            Pane pane = loader.load();
            C controller = loader.getController();

            return new FXMLData<>(pane, controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <C extends ClosingObservable> void setupScreen(FXMLData<C> data) {
        pnlRoot
            .getChildren()
            .setAll(data.pane());

        data
            .controller()
            .performWhenClosed(this::restoreDefaultScreen);
    }



    private record FXMLData<C extends ClosingObservable>(Pane pane, C controller) {}
}
