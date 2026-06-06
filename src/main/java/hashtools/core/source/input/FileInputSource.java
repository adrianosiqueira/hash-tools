package hashtools.core.source.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;

public class FileInputSource implements InputSource {

    private String filePath;



    public FileInputSource(String filePath) {
        this.filePath = Objects.requireNonNullElse(filePath, "");
    }



    @Override
    public boolean isValid() {
        Path path = Path.of(filePath);
        return Files.isRegularFile(path);
    }

    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        Path path = Path.of(filePath);

        try (InputStream stream = Files.newInputStream(path)) {
            int oneMebibyte = 1024 * 1024;
            int eof = -1;
            int bufferOffset = 0;

            byte[] buffer = new byte[oneMebibyte];
            int read;

            while ((read = stream.read(buffer)) != eof) {
                messageDigest.update(buffer, bufferOffset, read);
            }
        }
    }

    @Override
    public String identify() {
        return Path
            .of(filePath)
            .toAbsolutePath()
            .toString();
    }
}
