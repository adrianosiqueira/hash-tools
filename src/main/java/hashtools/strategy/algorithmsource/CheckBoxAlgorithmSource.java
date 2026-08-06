package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.Optional;

public class CheckBoxAlgorithmSource implements AlgorithmSource {

    private Pane pane;



    public CheckBoxAlgorithmSource(Pane pane) {
        this.pane = pane;
    }



    @Override
    public Collection<Algorithm> getAlgorithms() {
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
        if (pane == null) {
            return Optional.of("The pane is null");
        } else {
            return Optional.empty();
        }
    }
}
