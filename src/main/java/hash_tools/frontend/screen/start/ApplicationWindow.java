package hash_tools.frontend.screen.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_SCREEN_PATH));

        Pane pnlRoot = loader.load();
        changeScene(pnlRoot);

        stage.setTitle("Hash Tools");
        stage.setScene(SCENE);
        stage.show();
    }
}
