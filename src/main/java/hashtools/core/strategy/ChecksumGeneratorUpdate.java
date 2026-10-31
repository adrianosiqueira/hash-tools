package hashtools.core.strategy;

import hashtools.core.domain.ChecksumGenerator;

import java.io.IOException;
import java.util.Collection;

public interface ChecksumGeneratorUpdate {

    void update(Collection<ChecksumGenerator> generators)
    throws InterruptedException, IOException;
}
