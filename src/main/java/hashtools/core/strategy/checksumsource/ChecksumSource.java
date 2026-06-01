package hashtools.core.strategy.checksumsource;

import hashtools.core.model.Checksum;

import java.io.IOException;
import java.util.List;

public interface ChecksumSource {

    boolean isValid();

    List<Checksum> getValidChecksums() throws IOException;
}
