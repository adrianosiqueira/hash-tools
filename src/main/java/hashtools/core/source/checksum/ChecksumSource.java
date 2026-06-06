package hashtools.core.source.checksum;

import hashtools.core.checksum.Checksum;

import java.io.IOException;
import java.util.List;

public interface ChecksumSource {

    boolean isValid();

    List<Checksum> getValidChecksums() throws IOException;
}
