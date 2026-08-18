package hashtools.strategy.problemdetection;

import java.util.Optional;

public interface ProblemDetection {

    default Optional<String> detect() {
        return Optional.empty();
    }
}
