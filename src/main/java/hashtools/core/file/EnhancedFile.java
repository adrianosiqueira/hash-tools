package hashtools.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EnhancedFile {

    private Path file;



    private EnhancedFile(Path file) {
        this.file = Objects
            .requireNonNull(file)
            .toAbsolutePath();
    }



    public static Optional<EnhancedFile> filePath(String filePath) {
        return Optional
            .ofNullable(filePath)
            .map(Path::of)
            .map(EnhancedFile::new);
    }

    public static Optional<EnhancedFile> fileReference(AtomicReference<File> fileReference) {
        return Optional
            .ofNullable(fileReference)
            .map(AtomicReference::get)
            .map(File::toPath)
            .map(EnhancedFile::new);
    }



    public boolean exists() {
        return Files.exists(file);
    }

    public boolean isRegularFile() {
        return Files.isRegularFile(file);
    }

    public boolean hasFileExtension(FileExtension extension) {
        String fileExtension = this.getFileExtension();
        return extension.containsExtension(fileExtension);
    }

    public String getAbsolutePath() {
        return file.toString();
    }

    public void replaceContent(String content) throws IOException {
        Files.writeString(
            file,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    @Override
    public String toString() {
        return file.toString();
    }



    private String getFileExtension() {
        String filePath = file
            .getFileName()
            .toString();

        int dotIndex = filePath.lastIndexOf('.');

        return dotIndex > 0
            ? filePath.substring(dotIndex + 1)
            : "";
    }
}
