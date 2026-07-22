package hashtools.strategy.problemdetection;

import java.util.Optional;

public interface ProblemDetection {

    default Optional<String> detectProblem() {
        return Optional.empty();
    }
}
