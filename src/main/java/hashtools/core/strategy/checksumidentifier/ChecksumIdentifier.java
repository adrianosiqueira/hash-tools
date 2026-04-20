package hashtools.core.strategy.checksumidentifier;

public interface ChecksumIdentifier {

    static ChecksumIdentifier nullImplementation() {
        return () -> "";
    }



    String getIdentification() throws Exception;
}
