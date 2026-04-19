package hashtools.core.strategy.checksumidentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class StringChecksumIdentifier implements ChecksumIdentifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringChecksumIdentifier.class);



    private final String string;



    public StringChecksumIdentifier(String string) {
        this.string = Optional
            .ofNullable(string)
            .orElse("");
    }



    @Override
    public String getIdentification() {
        LOGGER.info("The string was identified as '{}'.", string);
        return string;
    }
}
