package hashtools.core.strategy.messagedigest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;

public class FileMessageDigestUpdate implements MessageDigestUpdate {

    private Path file;



    public FileMessageDigestUpdate(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public void update(MessageDigest messageDigest) throws RuntimeException {
        try (InputStream stream = Files.newInputStream(file)) {
            int oneMebibyte = 1024 * 1024;
            int endOfFile = -1;
            int bufferOffset = 0;

            byte[] buffer = new byte[oneMebibyte];
            int read;

            while ((read = stream.read(buffer)) != endOfFile) {
                messageDigest.update(buffer, bufferOffset, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain the bytes from the file. Try again or report it to the developer.", e);
        }
    }
}
