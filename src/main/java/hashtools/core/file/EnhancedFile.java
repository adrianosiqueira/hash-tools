package hashtools.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EnhancedFile {

    private Path path;



    private EnhancedFile(Path path) {
        this.path = path;
    }



    public static EnhancedFile create(Path path) {
        return Optional
            .ofNullable(path)
            .map(EnhancedFile::new)
            .orElse(null);
    }

    public static EnhancedFile create(File file) {
        return Optional
            .ofNullable(file)
            .map(File::toPath)
            .map(EnhancedFile::new)
            .orElse(null);
    }

    public static EnhancedFile create(String filePath) {
        return Optional
            .ofNullable(filePath)
            .map(Path::of)
            .map(EnhancedFile::new)
            .orElse(null);
    }

    public static EnhancedFile create(AtomicReference<File> fileReference) {
        return Optional
            .ofNullable(fileReference)
            .map(AtomicReference::get)
            .map(File::toPath)
            .map(EnhancedFile::new)
            .orElse(null);
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



    public void appendContent(String content) throws IOException {
        Files.writeString(
            path,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    public void replaceContent(String content) throws IOException {
        Files.writeString(
            path,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public void clearContent() throws IOException {
        this.replaceContent("");
    }

    public void appendNewLine() throws IOException {
        this.appendContent(System.lineSeparator());
    }
}
