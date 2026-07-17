package hashtools.strategy.algorithmsource;

import hashtools.domain.algorithm.Algorithm;
import javafx.scene.control.ComboBox;

import java.util.List;

public class ComboBoxAlgorithmSource implements AlgorithmSource {

    private ComboBox<Algorithm> comboBox;



    public ComboBoxAlgorithmSource(ComboBox<Algorithm> comboBox) {
        this.comboBox = comboBox;
    }



    @Override
    public List<Algorithm> getAlgorithms() {
        return List.of(comboBox.getValue());
    }
}
