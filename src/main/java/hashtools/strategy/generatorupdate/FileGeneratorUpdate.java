package hashtools.strategy.generatorupdate;

import hashtools.domain.checksum.ChecksumGenerator;
import hashtools.domain.commom.Result;
import hashtools.domain.file.EnhancedFile;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

public class FileGeneratorUpdate implements GeneratorUpdate {

    private static final int ONE_MEBIBYTE = 1024 * 1024;
    private static final int END_OF_FILE = -1;

    private EnhancedFile file;



    public FileGeneratorUpdate(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
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
                    return new Result.Error<>("Update canceled");
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
}
