package hashtools.strategy.inputsource;

import hashtools.domain.file.EnhancedFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;

public class FileInputSource implements InputSource {

    public static final int ONE_MEBIBYTE = 1024 * 1024;
    public static final int END_OF_FILE = -1;
    public static final int BUFFER_OFFSET = 0;



    private Collection<MessageDigest> messageDigests;
    private byte[] buffer;
    private int bytesRead;

    private EnhancedFile file;
    private boolean canceled;



    public FileInputSource(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
        this.canceled = false;
    }



    @Override
    public void updateMessageDigest(Collection<MessageDigest> messageDigests) throws IOException {
        this.messageDigests = messageDigests;
        this.buffer = new byte[ONE_MEBIBYTE];

        try (InputStream stream = file.getInputStream()) {
            while (!this.isCanceled() && this.readFile(stream)) {
                this.updateAllAlgorithms();
            }
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

    @Override
    public void cancel() {
        canceled = true;
    }



    private boolean readFile(InputStream stream) throws IOException {
        bytesRead = stream.read(buffer);
        return bytesRead != END_OF_FILE;
    }

    private boolean isCanceled() {
        return canceled;
    }

    private void updateAllAlgorithms() {
        messageDigests.forEach(messageDigest -> messageDigest.update(
            buffer,
            BUFFER_OFFSET,
            bytesRead
        ));
    }
}
