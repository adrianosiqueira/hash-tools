package hash_tools.backend.checksum_source;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class FileChecksumSource extends ChecksumSource {

    private static final int ONE_MEBIBYTE = 1048576;
    private static final int BUFFER_OFFSET = 0;
    private static final int EOF = -1;

    private final Path file;



    public FileChecksumSource(Path file) {
        this.file = file;
    }



    @Override
    public String identify() {
        return file
            .getFileName()
            .toString();
    }

    @Override
    protected void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
        try (InputStream stream = Files.newInputStream(file)) {
            byte[] buffer = new byte[ONE_MEBIBYTE];
            int bytesRead;

            while ((bytesRead = stream.read(buffer)) != EOF) {
                messageDigest.update(
                    buffer,
                    BUFFER_OFFSET,
                    bytesRead
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
