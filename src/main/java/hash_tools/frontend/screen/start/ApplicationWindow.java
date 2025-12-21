package hash_tools.frontend.screen.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    private static final String MAIN_SCREEN_PATH = "/hash_tools/frontend/screen/start/main-screen.fxml";



    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_SCREEN_PATH));

        Pane pnlRoot = loader.load();
        Scene scene = new Scene(pnlRoot);

        stage.setTitle("Hash Tools");
        stage.setScene(scene);
        stage.show();
    }
}
