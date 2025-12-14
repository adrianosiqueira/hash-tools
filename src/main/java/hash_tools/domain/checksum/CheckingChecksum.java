package hash_tools.domain.checksum;

public record CheckingChecksum(
    Checksum officialChecksum,
    Checksum generatedChecksum
) {

    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
    }

    public boolean isValid() {
        return officialChecksum != null
            && generatedChecksum != null
            && officialChecksum.isValid()
            && generatedChecksum.isValid();
    }
}
