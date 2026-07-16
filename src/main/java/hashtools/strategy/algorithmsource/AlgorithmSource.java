package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;

import java.util.List;
import java.util.Optional;

public interface AlgorithmSource {

    default List<Algorithm> getAlgorithms() {
        return List.of();
    }

    default Optional<String> detectProblem() {
        return Optional.empty();
    }
}
