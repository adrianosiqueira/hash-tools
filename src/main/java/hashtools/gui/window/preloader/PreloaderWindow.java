package hashtools.gui.window.preloader;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Window that will hold the preloader screen.
 * </p>
 *
 * @author Adriano Siqueira
 * @version 1.1.0
 * @since 2.0.0
 */
public class PreloaderWindow extends Preloader {

    private Stage stage;


    private void closeAfterDelay() {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}

            Platform.runLater(stage::close);
        }).start();
    }

    private void configureStage(Stage stage) {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(createScene());
        stage.show();
    }

    private Scene createScene() {
        return Optional.ofNullable(getClass().getResource("Preloader.fxml"))
                       .map(this::loadFxml)
                       .map(Scene::new)
                       .orElse(null);
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) closeAfterDelay();
    }

    private void loadFavIcon(Stage stage) {
        Optional.ofNullable(getClass().getResourceAsStream("/hashtools/gui/image/application-icon.png"))
                .map(Image::new)
                .ifPresent(stage.getIcons()::add);
    }

    private Parent loadFxml(URL url) {
        try {
            return FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(Stage stage) {
        loadFavIcon(stage);
        configureStage(stage);
    }
}
