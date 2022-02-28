package hashtools.core.model;

import hashtools.core.language.LanguageManager;
import javafx.stage.FileChooser;
import lombok.Getter;

@Getter
public enum FileExtension {

    ALL("All", "*"),
    HASH("Hashes", "*.md5", "*.sha1", "*.sha224", "*.sha256", "*.sha384", "*.sha512", "*.txt");


    private final String   description;
    private final String[] extensions;


    FileExtension(String description, String... extensions) {
        this.description = description;
        this.extensions = extensions;
    }


    public FileChooser.ExtensionFilter getFilter() {
        return new FileChooser.ExtensionFilter(LanguageManager.get(description), extensions);
    }
}
