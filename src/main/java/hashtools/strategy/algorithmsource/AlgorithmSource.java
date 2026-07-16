package hashtools.strategy.algorithmsource;

import hashtools.domain.checksum.Algorithm;

import java.util.List;
import java.util.Optional;

public interface AlgorithmSource {

    List<Algorithm> getAlgorithms();

    Optional<String> detectProblem();
}
