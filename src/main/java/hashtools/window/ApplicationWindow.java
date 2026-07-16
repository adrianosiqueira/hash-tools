package hashtools.window;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationWindow extends Application {

    // These values make the aspect ratio 16:9
    private static final double WIDTH = 853.3;
    private static final double HEIGHT = 480.0;



    @Override
    public void start(Stage stage) {
        try {
            JavaFxLoader loader = new JavaFxLoader();
            loader.setLocation("/hashtools/fxml/application.fxml");
            loader.load();
            loader.consumeScene(stage::setScene);
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        stage.setTitle("Hash Tools");
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.show();
    }
}
