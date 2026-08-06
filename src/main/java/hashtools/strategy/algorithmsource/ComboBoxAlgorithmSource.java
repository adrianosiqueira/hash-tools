package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;
import javafx.scene.control.ComboBox;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ComboBoxAlgorithmSource implements AlgorithmSource {

    private ComboBox<Algorithm> comboBox;



    public ComboBoxAlgorithmSource(ComboBox<Algorithm> comboBox) {
        this.comboBox = comboBox;
    }



    @Override
    public Collection<Algorithm> getAlgorithms() {
        return List.of(comboBox.getValue());
    }

    @Override
    public Optional<String> detectProblem() {
        if (comboBox == null) {
            return Optional.of("The combobox is null");
        } else {
            return Optional.empty();
        }
    }
}
