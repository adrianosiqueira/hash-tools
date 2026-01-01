package hash_tools.frontend.window;

import hash_tools.frontend.javafx.JavaFxFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    @Override
    public void start(Stage stage) {
        new JavaFxFile()
            .location("/hash_tools/frontend/screen/start/start-main-screen.fxml")
            .consumePane(pane -> changeScene(pane, stage))
            .handleException(Exception::printStackTrace)
            .process();

        stage.setTitle("Hash Tools");
        stage.show();
    }



    private void changeScene(Region pane, Stage stage) {
        Scene scene = new Scene(pane);
        stage.setScene(scene);
    }
}
