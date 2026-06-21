package hashtools.module.application.controller;

import hashtools.module.application.service.ApplicationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    @FXML
    private Pane pnlContent;

    private ApplicationService applicationService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.applicationService = new ApplicationService();
    }



    @FXML
    private void openCheckerScreen() {
        applicationService.openCheckerScreen(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openComparatorScreen() {
        applicationService.openComparatorScreen(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openGeneratorScreen() {
        applicationService.openGeneratorScreen(pnlContent.getChildren()::setAll);
    }
}
