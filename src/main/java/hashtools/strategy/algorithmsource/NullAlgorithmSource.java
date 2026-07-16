package hashtools.strategy.algorithmsource;

import hashtools.domain.checksum.Algorithm;

import java.util.List;
import java.util.Optional;

public class NullAlgorithmSource implements AlgorithmSource {

    @Override
    public List<Algorithm> getAlgorithms() {
        return List.of();
    }

    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
