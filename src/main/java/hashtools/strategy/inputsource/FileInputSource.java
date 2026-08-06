package hashtools.strategy.inputsource;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.file.EnhancedFile;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

public class FileInputSource implements InputSource {

    public static final int ONE_MEBIBYTE = 1024 * 1024;
    public static final int END_OF_FILE = -1;



    private Collection<ChecksumGenerator> generators;
    private byte[] buffer;
    private int bytesRead;

    private EnhancedFile file;
    private boolean canceled;



    public FileInputSource(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
        this.canceled = false;
    }



    @Override
    public Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        this.generators = generators;
        this.buffer = new byte[ONE_MEBIBYTE];

        try (InputStream stream = file.getInputStream()) {
            while (this.isNotCanceled() && this.readFile(stream)) {
                this.updateAllAlgorithms();
            }
        } catch (Exception e) {
            return new ExceptionResult(e);
        }

        return this.isCanceled()
            ? new CanceledResult()
            : new SuccessResult();
    }

    @Override
    public void cancelChecksumGeneratorsUpdate() {
        canceled = true;
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
        } else {
            return Optional.empty();
        }
    }



    private boolean readFile(InputStream stream) throws IOException {
        bytesRead = stream.read(buffer);
        return bytesRead != END_OF_FILE;
    }

    private boolean isCanceled() {
        return canceled;
    }

    private boolean isNotCanceled() {
        return !canceled;
    }

    private void updateAllAlgorithms() {
        generators.forEach(messageDigest -> messageDigest.receiveBytes(
            buffer,
            bytesRead
        ));
    }
}
