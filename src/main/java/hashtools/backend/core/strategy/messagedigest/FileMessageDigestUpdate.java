package hashtools.backend.core.strategy.messagedigest;

import hashtools.backend.core.file.EnhancedFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileMessageDigestUpdate implements MessageDigestUpdate {

    private EnhancedFile file;



    public FileMessageDigestUpdate(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElseThrow();
    }



    @Override
    public void update(MessageDigest messageDigest) throws RuntimeException {
        try (InputStream stream = file.getInputStream()) {
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
