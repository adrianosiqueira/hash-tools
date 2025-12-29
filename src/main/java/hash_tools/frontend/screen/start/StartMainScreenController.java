package hash_tools.frontend.screen.start;

import hash_tools.frontend.fxml.FXMLFile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class StartMainScreenController implements Initializable {

    @FXML
    private Pane pnlRoot;
    @FXML
    private Pane pnlContent;

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
        new FXMLFile()
            .location("/hash_tools/frontend/screen/checker/checker-main-screen.fxml")
            .resources(resources)
            .load()
            .usePane(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openGeneratorScreen() {
        new FXMLFile()
            .location("/hash_tools/frontend/screen/generator/generator-main-screen.fxml")
            .resources(resources)
            .load()
            .usePane(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openComparatorScreen() {
        new FXMLFile()
            .location("/hash_tools/frontend/screen/comparator/comparator-main-screen.fxml")
            .resources(resources)
            .load()
            .usePane(pnlContent.getChildren()::setAll);
    }



    private void startSplash() {
        pnlRoot.setCursor(Cursor.WAIT);
        pnlRoot
            .getChildren()
            .forEach(node -> node.setDisable(true));
    }

    private void stopSplash() {
        pnlRoot.setCursor(Cursor.DEFAULT);
        pnlRoot
            .getChildren()
            .forEach(node -> node.setDisable(false));
    }
}
