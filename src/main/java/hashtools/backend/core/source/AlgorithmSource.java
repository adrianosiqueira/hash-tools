package hashtools.backend.core.source;

import hashtools.backend.core.checksum.Algorithm;
import hashtools.backend.core.strategy.algorithm.AlgorithmRetrieval;
import hashtools.backend.core.strategy.algorithm.CheckBoxAlgorithmRetrieval;
import hashtools.backend.core.strategy.problem.AlgorithmListProblemDetection;
import hashtools.backend.core.strategy.problem.ProblemDetection;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class AlgorithmSource {

    private AlgorithmRetrieval algorithmRetrieval;
    private ProblemDetection problemDetection;



    private AlgorithmSource(AlgorithmRetrieval algorithmRetrieval) {
        this.algorithmRetrieval = algorithmRetrieval;
        this.problemDetection = new AlgorithmListProblemDetection(this);
    }



    public static AlgorithmSource checkBoxAlgorithmSource(Pane pane) {
        return new AlgorithmSource(
            new CheckBoxAlgorithmRetrieval(pane)
        );
    }

    public static AlgorithmSource nullAlgorithmSource() {
        return new AlgorithmSource(
            List::of
        );
    }



    public Optional<String> detectProblem() {
        return problemDetection.detect();
    }

    public List<Algorithm> getAlgorithms() {
        return algorithmRetrieval.retrieve();
    }
}
