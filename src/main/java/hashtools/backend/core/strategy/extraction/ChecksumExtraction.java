package hashtools.backend.core.strategy.extraction;

import hashtools.backend.core.checksum.Checksum;

import java.util.List;

public interface ChecksumExtraction {

    List<Checksum> extract() throws RuntimeException;
}
