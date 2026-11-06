package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;

import java.util.Objects;

public class StringInputProblemDetection implements ProblemDetection {

    private String string;



    public StringInputProblemDetection(String string) {
        this.string = Objects.requireNonNull(string);
    }



    @Override
    public void detect()
    throws ProblemException {
    }
}
