package hash_tools.frontend.screen.start;

import hash_tools.frontend.window.FXMLFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    private static final String MAIN_SCREEN_PATH = "/hash_tools/frontend/screen/start/main-screen.fxml";
    private static final Scene SCENE = new Scene(new Pane());



    public static void changeScene(Pane pane) {
        SCENE.setRoot(pane);
    }

    public static Pane getSceneContent() {
        return (Pane) SCENE.getRoot();
    }



    @Override
    public void start(Stage stage) {
        new FXMLFile()
            .location(MAIN_SCREEN_PATH)
            .load()
            .usePane(ApplicationWindow::changeScene);

        stage.setTitle("Hash Tools");
        stage.setScene(SCENE);
        stage.show();
    }
}
