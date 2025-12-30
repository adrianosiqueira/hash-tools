package hash_tools.frontend.window;

import hash_tools.frontend.javafx.JavaFxFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    private static final Scene SCENE = new Scene(new Pane());



    public static void changeScene(Pane pane) {
        SCENE.setRoot(pane);
    }

    public static Pane getSceneContent() {
        return (Pane) SCENE.getRoot();
    }



    @Override
    public void start(Stage stage) {
        new JavaFxFile()
            .location("/hash_tools/frontend/screen/start/start-main-screen.fxml")
            .consumePane(ApplicationWindow::changeScene)
            .process();

        stage.setTitle("Hash Tools");
        stage.setScene(SCENE);
        stage.show();
    }
}
