package hashtools.backend.core.strategy.problem;

import hashtools.backend.core.checksum.Algorithm;
import hashtools.backend.core.source.AlgorithmSource;

import java.util.List;
import java.util.Optional;

public class AlgorithmListProblemDetection implements ProblemDetection {

    private AlgorithmSource algorithmSource;



    public AlgorithmListProblemDetection(AlgorithmSource algorithmSource) {
        this.algorithmSource = algorithmSource;
    }



    @Override
    public Optional<String> detect() {
        List<Algorithm> algorithms = algorithmSource.getAlgorithms();

        if (algorithms.isEmpty()) {
            return Optional.of("There is no algorithms selected");
        }

        return Optional.empty();
    }
}
