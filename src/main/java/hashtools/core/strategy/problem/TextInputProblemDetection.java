package hashtools.core.strategy.problem;

import java.util.Objects;
import java.util.Optional;

public class TextInputProblemDetection implements ProblemDetection {

    private String text;



    public TextInputProblemDetection(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public Optional<String> detect() {
        return Optional.empty();
    }
}
