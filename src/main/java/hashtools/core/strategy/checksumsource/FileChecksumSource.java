package hashtools.core.strategy.checksumsource;

import hashtools.core.checksum.Checksum;
import hashtools.view.dialog.FileExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileChecksumSource implements ChecksumSource {

    private String filePath;



    public FileChecksumSource(String filePath) {
        this.filePath = Objects.requireNonNullElse(filePath, "");
    }



    @Override
    public boolean isValid() {
        String fileExtension = this.getFileExtension();

        if (!FileExtension.HASH.containsExtension(fileExtension)) {
            return false;
        }



        Path path = Path.of(filePath);
        return Files.isRegularFile(path);
    }

    @Override
    public List<Checksum> getValidChecksums() throws IOException {
        List<Checksum> checksums = new ArrayList<>();



        Path path = Path.of(filePath);

        try (Stream<String> lines = Files.lines(path)) {
            lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .forEach(checksums::add);
        }



        return checksums;
    }



    private String getFileExtension() {
        int lastDotIndex = filePath.lastIndexOf('.');

        return lastDotIndex > 0
            ? filePath.substring(lastDotIndex + 1)
            : "";
    }
}
