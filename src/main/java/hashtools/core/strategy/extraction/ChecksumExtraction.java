package hashtools.core.strategy.extraction;

import hashtools.core.checksum.Checksum;

import java.util.List;

public interface ChecksumExtraction {

    List<Checksum> extract() throws RuntimeException;
}
