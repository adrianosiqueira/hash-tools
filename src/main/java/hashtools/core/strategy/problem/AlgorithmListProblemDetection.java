package hashtools.core.strategy.problem;

import hashtools.core.checksum.Algorithm;
import hashtools.core.source.AlgorithmSource;

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
