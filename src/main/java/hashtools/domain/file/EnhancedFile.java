package hashtools.domain.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class EnhancedFile {

    private Path file;



    private EnhancedFile(Path file) {
        this.file = Objects
            .requireNonNull(file)
            .toAbsolutePath();
    }



    public static EnhancedFile filePath(String filePath) {
        Path path = Paths.get(filePath);
        return new EnhancedFile(path);
    }

    public static EnhancedFile fileReference(AtomicReference<File> fileReference) {
        Path path = fileReference
            .get()
            .toPath();

        return new EnhancedFile(path);
    }

    public static EnhancedFile file(File file) {
        Path path = file.toPath();
        return new EnhancedFile(path);
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

    public Stream<String> getLines() throws IOException {
        return Files.lines(file);
    }

    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file);
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
