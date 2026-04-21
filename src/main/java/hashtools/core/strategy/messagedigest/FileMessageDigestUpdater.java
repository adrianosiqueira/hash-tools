package hashtools.core.strategy.messagedigest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Optional;

public class FileMessageDigestUpdater implements MessageDigestUpdater {

    private static final int ONE_MEBIBYTE = 1_048_576;
    private static final int END_OF_FILE = -1;
    private static final int BUFFER_OFFSET = 0;



    private final String filePath;



    public FileMessageDigestUpdater(String filePath) {
        this.filePath = Optional
            .ofNullable(filePath)
            .orElse("");
    }



    @Override
    public void update(MessageDigest messageDigest) throws Exception {
        Path file = Path.of(filePath);

        if (Files.isDirectory(file)) {
            throw new IllegalArgumentException("The path is a directory: " + file.toAbsolutePath());
        }



        byte[] buffer = new byte[ONE_MEBIBYTE];
        int read;

        try (InputStream stream = Files.newInputStream(file)) {
            while ((read = stream.read(buffer)) != END_OF_FILE) {
                messageDigest.update(
                    buffer,
                    BUFFER_OFFSET,
                    read
                );
            }
        }
    }
}
