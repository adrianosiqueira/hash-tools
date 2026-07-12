package hashtools.backend.core.strategy.inputsource;

import hashtools.backend.core.file.EnhancedFile;
import hashtools.backend.core.interfaces.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Optional;

public class FileInputSource implements InputSource {

    private EnhancedFile file;



    public FileInputSource(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElse(null);
    }



    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
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

    @Override
    public String getIdentification() {
        return file.getAbsolutePath();
    }

    @Override
    public Optional<String> detectProblem() {
        if (!file.exists()) {
            return Optional.of("The input file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The input file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }
}
