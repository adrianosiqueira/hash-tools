package hashtools.backend.core.strategy.problem;

import java.util.Objects;
import java.util.Optional;

public class ChecksumTextProblemDetection implements ProblemDetection {

    private String text;



    public ChecksumTextProblemDetection(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public Optional<String> detect() {
        return Optional.empty();
    }
}
