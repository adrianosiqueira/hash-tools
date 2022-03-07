package hashtools.core.consumer;

import hashtools.core.model.SampleContainer;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckerGUISampleContainerConsumer implements SampleContainerConsumer {

    private final ProgressBar progressBar;
    private final Label       label;


    @Override
    public void accept(SampleContainer sampleContainer) {
        progressBar.setProgress(sampleContainer.getReliabilityPercentage());
        label.setText(formatResult(sampleContainer));
    }
}
