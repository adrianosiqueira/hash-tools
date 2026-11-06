package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;

import java.util.Objects;

public class StringChecksumProblemDetection implements ProblemDetection {

    private String string;



    public StringChecksumProblemDetection(String string) {
        this.string = Objects.requireNonNull(string);
    }



    @Override
    public void detect()
    throws ProblemException {
        if (string.isBlank()) {
            throw new ProblemException("String is empty");
        }
    }
}
