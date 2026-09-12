package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.commom.Result;
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

    @Override
    public Result<Void, String> updateGenerators(Collection<ChecksumGenerator> generators, Consumer<Double> progressTracker) {
        try (var inputStream = file.getInputStream()) {
            // Setup
            var buffer = new byte[ONE_MEBIBYTE];
            var bytesRead = 0;

            // Progress tracking
            var requiredCycles = (double) file.getSizeInBytes() / ONE_MEBIBYTE;
            var runCycles = 0;
            progressTracker.accept(0.0);

            // Processing
            while ((bytesRead = inputStream.read(buffer)) != END_OF_FILE) {
                if (Thread.currentThread().isInterrupted()) {
                    // Operation has been canceled
                    return new Result.Ok<>(null);
                }

                for (var generator : generators) {
                    generator.receiveBytes(buffer, bytesRead);
                }

                runCycles++;
                progressTracker.accept(runCycles / requiredCycles);
            }

            return new Result.Ok<>(null);
        } catch (IOException e) {
            return new Result.Error<>(e.getMessage());
        } catch (Exception e) {
            return new Result.Error<>("Failed to update the generators");
        } finally {
            progressTracker.accept(1.0);
        }
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
