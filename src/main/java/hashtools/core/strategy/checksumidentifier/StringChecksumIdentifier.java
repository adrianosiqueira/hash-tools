package hashtools.core.strategy.checksumidentifier;

import java.util.Optional;

public class StringChecksumIdentifier implements ChecksumIdentifier {

    private final String string;



    public StringChecksumIdentifier(String string) {
        this.string = Optional
            .ofNullable(string)
            .orElse("");
    }



    @Override
    public String getIdentification() throws Exception {
        return string;
    }
}
