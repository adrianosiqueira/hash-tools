package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;

public class CheckBoxAlgorithmSource implements AlgorithmSource {

    private Pane pane;



    public CheckBoxAlgorithmSource(Pane pane) {
        this.pane = pane;
    }



    @Override
    public List<Algorithm> getAlgorithms() {
        return pane
            .getChildren()
            .stream()
            .filter(CheckBox.class::isInstance)
            .map(CheckBox.class::cast)
            .filter(CheckBox::isSelected)
            .map(CheckBox::getText)
            .map(Algorithm::getByName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    @Override
    public Optional<String> detectProblem() {
        List<Algorithm> algorithms = this.getAlgorithms();

        if (algorithms.isEmpty()) {
            return Optional.of("There is no algorithms selected");
        }

        return Optional.empty();
    }
}
