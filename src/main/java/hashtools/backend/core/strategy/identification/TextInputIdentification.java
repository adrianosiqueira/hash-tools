package hashtools.backend.core.strategy.identification;

import java.util.Objects;

public class TextInputIdentification implements InputIdentification {

    private String text;



    public TextInputIdentification(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public String identify() {
        return text;
    }
}
