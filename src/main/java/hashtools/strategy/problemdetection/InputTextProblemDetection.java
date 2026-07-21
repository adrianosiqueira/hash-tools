package hashtools.strategy.problemdetection;

import java.util.Optional;

public class InputTextProblemDetection implements ProblemDetection {

    private String text;



    public InputTextProblemDetection(String text) {
        this.text = text;
    }



    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
