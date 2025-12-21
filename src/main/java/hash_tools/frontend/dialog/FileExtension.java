package hash_tools.frontend.dialog;

import javafx.stage.FileChooser;

import java.util.ResourceBundle;

public record FileExtension(
    String description,
    String... extensions
) {

    public static final FileExtension ALL = new FileExtension("All", "*.*");
    public static final FileExtension CHECKSUM = new FileExtension("Checksum", "*.*");



    public FileChooser.ExtensionFilter toFilter(ResourceBundle resources) {
        return new FileChooser.ExtensionFilter(
            resources.getString(description),
            extensions
        );
    }

    public FileChooser.ExtensionFilter toFilter() {
        return new FileChooser.ExtensionFilter(
            description,
            extensions
        );
    }
}
