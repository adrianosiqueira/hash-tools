package hashtools.checker.strategy;

import hashtools.checker.domain.OfficialChecksums;

import java.io.IOException;

public interface ChecksumExtraction {

    OfficialChecksums extract()
    throws IOException;
}
