package hashtools.backend.application.controller;

import hashtools.backend.application.service.ApplicationService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
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
        this.applicationService.openCheckerScreen(pnlContent.getChildren()::setAll);
    }



    @FXML
    private void openCheckerScreen(Event event) {
        ToggleButton button = (ToggleButton) event.getSource();
        button.setSelected(true);

        applicationService.openCheckerScreen(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openComparatorScreen(Event event) {
        ToggleButton button = (ToggleButton) event.getSource();
        button.setSelected(true);

        applicationService.openComparatorScreen(pnlContent.getChildren()::setAll);
    }

    @FXML
    private void openGeneratorScreen(Event event) {
        ToggleButton button = (ToggleButton) event.getSource();
        button.setSelected(true);

        applicationService.openGeneratorScreen(pnlContent.getChildren()::setAll);
    }
}
