package hashtools.backend.core.strategy.algorithm;

import hashtools.backend.core.checksum.Algorithm;

import java.util.List;

public interface AlgorithmRetrieval {

    List<Algorithm> retrieve();
}
