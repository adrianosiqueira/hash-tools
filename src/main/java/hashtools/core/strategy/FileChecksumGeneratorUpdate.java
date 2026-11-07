package hashtools.core.strategy;

import hashtools.core.domain.ChecksumGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class FileChecksumGeneratorUpdate implements ChecksumGeneratorUpdate {

    private static final int ONE_MEBIBYTE = 1024 * 1024;
    private static final int END_OF_FILE = -1;

    private Path file;



    public FileChecksumGeneratorUpdate(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public void update(Collection<ChecksumGenerator> generators)
    throws InterruptedException, IOException {
        var buffer = new byte[ONE_MEBIBYTE];
        var bytesRead = 0;

        try (var inputStream = Files.newInputStream(file)) {
            while ((bytesRead = inputStream.read(buffer)) != END_OF_FILE) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (var generator : generators) {
                    generator.update(buffer, bytesRead);
                }
            }
        }
    }
}
