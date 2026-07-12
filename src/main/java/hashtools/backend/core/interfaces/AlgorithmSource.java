package hashtools.backend.core.interfaces;

import hashtools.backend.core.checksum.Algorithm;

import java.util.List;
import java.util.Optional;

public interface AlgorithmSource {

    List<Algorithm> getAlgorithms();

    Optional<String> detectProblem();
}
