package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.file.EnhancedFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.function.Consumer;

public class FileGeneratorUpdate implements GeneratorUpdate {

    private static final int ONE_MEBIBYTE = 1024 * 1024;
    private static final int END_OF_FILE = -1;

    private EnhancedFile file;
    private boolean canceled;

    private byte[] buffer;
    private int bytesRead;



    public FileGeneratorUpdate(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
        this.canceled = false;
    }



    @Override
    public void update(Collection<ChecksumGenerator> generators, Consumer<Double> progressConsumer) throws RuntimeException {
        try (InputStream stream = file.getInputStream()) {
            this.buffer = new byte[ONE_MEBIBYTE];

            // Progress tracking
            double requiredCycles = (double) file.getSizeInBytes() / ONE_MEBIBYTE;
            int runCycles = 0;
            progressConsumer.accept(0.0);

            while (this.isNotCanceled() && this.readFile(stream)) {
                this.updateGenerators(generators);

                runCycles++;
                progressConsumer.accept(runCycles / requiredCycles);
            }

            progressConsumer.accept(1.0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancel() {
        canceled = true;
    }



    private boolean isNotCanceled() {
        return !canceled;
    }

    private boolean readFile(InputStream stream) throws IOException {
        bytesRead = stream.read(buffer);
        return bytesRead != END_OF_FILE;
    }

    private void updateGenerators(Collection<ChecksumGenerator> generators) {
        generators.forEach(generator -> generator.receiveBytes(
            buffer,
            bytesRead
        ));
    }
}
