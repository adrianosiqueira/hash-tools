package hashtools.backend.core.strategy.algorithmsource;

import hashtools.backend.core.checksum.Algorithm;
import hashtools.backend.core.interfaces.AlgorithmSource;

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
