package hashtools.backend.core.strategy.problem;

import java.util.Objects;
import java.util.Optional;

public class InputTextProblemDetection implements ProblemDetection {

    private String text;



    public InputTextProblemDetection(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public Optional<String> detect() {
        return Optional.empty();
    }
}
