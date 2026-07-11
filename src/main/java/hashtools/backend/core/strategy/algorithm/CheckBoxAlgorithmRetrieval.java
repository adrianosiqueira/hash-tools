package hashtools.backend.core.strategy.algorithm;

import hashtools.backend.core.checksum.Algorithm;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CheckBoxAlgorithmRetrieval implements AlgorithmRetrieval {

    private Pane pane;



    public CheckBoxAlgorithmRetrieval(Pane pane) {
        this.pane = Objects.requireNonNull(pane);
    }



    @Override
    public List<Algorithm> retrieve() {
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
}
