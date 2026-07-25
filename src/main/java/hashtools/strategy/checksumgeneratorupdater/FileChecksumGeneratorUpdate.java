package hashtools.strategy.checksumgeneratorupdater;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.file.EnhancedFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class FileChecksumGeneratorUpdate implements ChecksumGeneratorUpdate {

    private static final int ONE_MEBIBYTE = 1024 * 1024;
    private static final int END_OF_FILE = -1;



    private Collection<ChecksumGenerator> generators;
    private byte[] buffer;
    private int bytesRead;

    private EnhancedFile file;
    private boolean canceled;



    public FileChecksumGeneratorUpdate(String filePath) {
        this.file = EnhancedFile.filePath(filePath);
        this.canceled = false;
    }



    @Override
    public Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        this.generators = generators;
        this.buffer = new byte[ONE_MEBIBYTE];

        try (InputStream stream = file.getInputStream()) {
            while (!this.isCanceled() && this.readFile(stream)) {
                this.updateAllGenerators();
            }

            return new Result.Success();
        } catch (IOException e) {
            return new Result.Failure(e);
        }
    }

    @Override
    public void cancelChecksumGeneratorsUpdate() {
        canceled = true;
    }



    private boolean readFile(InputStream stream) throws IOException {
        bytesRead = stream.read(buffer);
        return bytesRead != END_OF_FILE;
    }

    private boolean isCanceled() {
        return canceled;
    }

    private void updateAllGenerators() {
        generators.forEach(generator -> generator.receiveBytes(
            buffer,
            bytesRead
        ));
    }
}
