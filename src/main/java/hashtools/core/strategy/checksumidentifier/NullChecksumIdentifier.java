package hashtools.core.strategy.checksumidentifier;

public class NullChecksumIdentifier implements ChecksumIdentifier {

    @Override
    public String getIdentification() {
        throw new IllegalStateException("The ChecksumIdentifier is not provided");
    }
}
