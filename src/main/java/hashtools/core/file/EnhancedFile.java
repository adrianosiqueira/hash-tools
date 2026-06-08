package hashtools.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class EnhancedFile {

    private Path path;



    public EnhancedFile(Path path) {
        this.path = Objects
            .requireNonNull(path, "The path cannot be null")
            .toAbsolutePath();
    }

    public EnhancedFile(File file) {
        this.path = Objects
            .requireNonNull(file, "The file cannot be null")
            .toPath()
            .toAbsolutePath();
    }

    public EnhancedFile(String filePath) {
        Objects.requireNonNull(filePath, "The file path cannot be null");

        this.path = Path
            .of(filePath)
            .toAbsolutePath();
    }



    public boolean exists() {
        return Files.exists(path);
    }

    public boolean isFile() {
        return Files.isRegularFile(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }



    public EnhancedFile appendContent(String content) throws IOException {
        Files.writeString(
            path,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );

        return this;
    }

    public EnhancedFile replaceContent(String content) throws IOException {
        Files.writeString(
            path,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );

        return this;
    }

    public EnhancedFile clearContent() throws IOException {
        return this.replaceContent("");
    }

    public EnhancedFile appendNewLine() throws IOException {
        return this.appendContent(System.lineSeparator());
    }
}
