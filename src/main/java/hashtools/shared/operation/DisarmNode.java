package hashtools.shared.operation;


import hashtools.shared.Resource;
import javafx.scene.Node;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DisarmNode extends Operation {

    private final Node node;

    @Override
    protected void perform() {
        node.pseudoClassStateChanged(
            Resource.PseudoClass.ARMED,
            false
        );
    }
}
