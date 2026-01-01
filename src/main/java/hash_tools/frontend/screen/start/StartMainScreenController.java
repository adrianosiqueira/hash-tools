package hash_tools.frontend.screen.start;

import hash_tools.frontend.abstraction.ProcessingObservable;
import hash_tools.frontend.javafx.JavaFxFile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class StartMainScreenController implements Initializable {

    @FXML
    private ScrollPane pnlRoot;
    @FXML
    private Pane pnlContent;
    @FXML
    private Pane pnlForm;

    @FXML
    private Pane pnlButton;
    @FXML
    private Button btnCheck;
    @FXML
    private Button btnGenerate;
    @FXML
    private Button btnCompare;


    private ResourceBundle resources;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        this.openCheckerScreen();
    }



    @FXML
    private void openCheckerScreen() {
        openScreen("/hash_tools/frontend/screen/checker/checker-main-screen.fxml");
    }

    @FXML
    private void openGeneratorScreen() {
        openScreen("/hash_tools/frontend/screen/generator/generator-main-screen.fxml");
    }

    @FXML
    private void openComparatorScreen() {
        openScreen("/hash_tools/frontend/screen/comparator/comparator-main-screen.fxml");
    }



    private void startSplash() {
        pnlRoot.setCursor(Cursor.WAIT);
        pnlContent
            .getChildren()
            .forEach(node -> node.setDisable(true));
    }

    private void stopSplash() {
        pnlRoot.setCursor(Cursor.DEFAULT);
        pnlContent
            .getChildren()
            .forEach(node -> node.setDisable(false));
    }



    private void openScreen(String location) {
        new JavaFxFile()
            .location(location)
            .resources(resources)
            .consumePane(pnlForm.getChildren()::setAll)
            .<ProcessingObservable>consumeController(c -> c.performWhenProcessingStarts(this::startSplash))
            .<ProcessingObservable>consumeController(c -> c.performWhenProcessingStops(this::stopSplash))
            .handleException(Exception::printStackTrace)
            .process();
    }
}
