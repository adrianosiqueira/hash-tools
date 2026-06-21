package hashtools.core.strategy.problem;

import java.util.Objects;
import java.util.Optional;

public class TextChecksumProblemDetection implements ProblemDetection {

    private String text;



    public TextChecksumProblemDetection(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public Optional<String> detect() {
        return Optional.empty();
    }
}
