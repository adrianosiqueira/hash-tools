package hashtools.core.strategy;

import java.util.Objects;

public class StringIdentification implements Identification {

    private String string;



    public StringIdentification(String string) {
        this.string = Objects.requireNonNull(string);
    }



    @Override
    public String identify() {
        return string;
    }
}
