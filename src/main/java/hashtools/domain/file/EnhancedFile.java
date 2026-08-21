package hashtools.domain.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class EnhancedFile {

    private Path path;



    private EnhancedFile() {
    }



    public static EnhancedFile createFromFilePath(String filePath) {
        EnhancedFile enhancedFile = new EnhancedFile();
        enhancedFile.path = Paths.get(filePath);

        return enhancedFile;
    }

    public static EnhancedFile createFromFile(File file) {
        EnhancedFile enhancedFile = new EnhancedFile();
        enhancedFile.path = file.toPath();

        return enhancedFile;
    }

    public static EnhancedFile createTemporaryFile() throws RuntimeException {
        try {
            EnhancedFile enhancedFile = new EnhancedFile();
            enhancedFile.path = Files.createTempFile("hash-tools-", ".tmp");
            return enhancedFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean exists() {
        return Files.exists(path);
    }

    public boolean isRegularFile() {
        return Files.isRegularFile(path);
    }

    public boolean hasFileExtension(FileExtension extension) {
        String fileExtension = this.getFileExtension();
        return extension.containsExtension(fileExtension);
    }

    public String getAbsolutePath() {
        return path
            .toAbsolutePath()
            .toString();
    }

    public long getSizeInBytes() throws IOException {
        return Files.size(path);
    }

    public void replaceContent(String content) throws IOException {
        Files.writeString(
            path,
            content,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public Stream<String> getLinesStream() throws IOException {
        return Files.lines(path);
    }

    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    public void delete() throws IOException {
        Files.deleteIfExists(path);
    }



    private String getFileExtension() {
        String filePath = path
            .getFileName()
            .toString();

        int dotIndex = filePath.lastIndexOf('.');

        return dotIndex > 0
            ? filePath.substring(dotIndex + 1)
            : "";
    }
}
