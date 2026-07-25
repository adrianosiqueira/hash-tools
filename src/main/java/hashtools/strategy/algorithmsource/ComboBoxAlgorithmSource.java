package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;
import javafx.scene.control.ComboBox;

import java.util.List;

public record ComboBoxAlgorithmSource(
    ComboBox<Algorithm> comboBox
) implements AlgorithmSource {

    @Override
    public List<Algorithm> getAlgorithms() {
        return List.of(comboBox.getValue());
    }
}
