package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumSource implements ChecksumSource {

    private EnhancedFile file;



    public FileChecksumSource(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
    }



    @Override
    public List<Checksum> extractOfficialChecksums() throws IOException {
        try (Stream<String> lines = file.getLines()) {
            return lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .toList();
        }
    }

    @Override
    public Optional<String> detectProblem() {
        if (!file.hasFileExtension(FileExtension.HASH)) {
            return Optional.of("The file is not a checksum file. Use the dialog selector to select a valid file.");
        } else if (!file.exists()) {
            return Optional.of("The checksum file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The checksum file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }
}
