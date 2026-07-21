package hashtools.strategy.problemdetection;

import java.util.Optional;

public class ChecksumTextProblemDetection implements ProblemDetection {

    private String text;



    public ChecksumTextProblemDetection(String text) {
        this.text = text;
    }



    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
