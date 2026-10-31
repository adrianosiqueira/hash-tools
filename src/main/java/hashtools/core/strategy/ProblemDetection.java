package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;

public interface ProblemDetection {

    void detect()
    throws ProblemException;
}
