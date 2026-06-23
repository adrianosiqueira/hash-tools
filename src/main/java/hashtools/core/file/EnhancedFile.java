package hashtools.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class EnhancedFile {

    private Path file;



    public EnhancedFile(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }

    public EnhancedFile(File file) {
        Objects.requireNonNull(file);
        this.file = file.toPath();
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
        return file
            .toAbsolutePath()
            .toString();
    }

    public void replaceContent(String content) throws IOException {
        Files.writeString(
            file,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
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
