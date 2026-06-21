package hashtools.view.window;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hash Tools");
        stage.setWidth(853);
        stage.setHeight(480);
        stage.show();
    }
}
