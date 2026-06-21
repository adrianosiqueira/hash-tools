package hashtools.core.file;

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



    public boolean exists() {
        return Files.exists(file);
    }

    public boolean isRegularFile() {
        return Files.isRegularFile(file);
    }

    public boolean hasFileExtension(FileExtension extension) {
        String filePath = file
            .getFileName()
            .toString();



        int dotIndex = filePath.lastIndexOf('.');

        String fileExtension = dotIndex > 0
            ? filePath.substring(dotIndex + 1)
            : "";


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
}
