package hash_tools.frontend.fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXMLFile {

    private URL location;
    private ResourceBundle resources;



    public FXMLFile location(URL location) {
        this.location = location;
        return this;
    }

    public FXMLFile location(String location) {
        this.location = this
            .getClass()
            .getResource(location);

        return this;
    }

    public FXMLFile resources(ResourceBundle resources) {
        this.resources = resources;
        return this;
    }

    public FXMLFile resources(String basename) {
        this.resources = ResourceBundle.getBundle(basename);
        return this;
    }

    public FXMLFile resources(String basename, Locale locale) {
        this.resources = ResourceBundle.getBundle(basename, locale);
        return this;
    }



    public <CONTROLLER_TYPE> FXMLData<CONTROLLER_TYPE> load() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Optional
                .ofNullable(resources)
                .ifPresent(loader::setResources);


            Pane pane = loader.load();
            CONTROLLER_TYPE controller = loader.getController();


            return new FXMLData<>(pane, controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
