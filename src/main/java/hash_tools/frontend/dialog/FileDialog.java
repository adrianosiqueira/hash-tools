package hash_tools.frontend.dialog;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FileDialog {

    private String title;
    private Window ownerWindow;
    private ResourceBundle resources;

    private List<FileExtension> extensions = new ArrayList<>();



    public static FileDialog startSetup() {
        return new FileDialog();
    }



    public FileDialog localized(ResourceBundle resources) {
        this.resources = resources;
        return this;
    }

    public FileDialog title(String title) {
        this.title = title;
        return this;
    }

    public FileDialog ownerWindow(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        return this;
    }

    public FileDialog addExtensionFilter(FileExtension extension) {
        this.extensions.add(extension);
        return this;
    }



    public Optional<Path> openFile() {
        return Optional
            .of(createDialog())
            .map(dialog -> dialog.showOpenDialog(ownerWindow))
            .map(File::toPath)
            .map(Path::toAbsolutePath);
    }



    private FileChooser createDialog() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(localizeTitle());
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));

        extensions
            .stream()
            .map(this::createExtensionFilters)
            .forEach(chooser.getExtensionFilters()::add);

        return chooser;
    }



    private String localizeTitle() {
        return Optional
            .ofNullable(resources)
            .map(r -> r.getString(title))
            .orElse(title);
    }

    private FileChooser.ExtensionFilter createExtensionFilters(FileExtension extension) {
        return Optional
            .ofNullable(resources)
            .map(extension::toFilter)
            .orElseGet(extension::toFilter);
    }
}
