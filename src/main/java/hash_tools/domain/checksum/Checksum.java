package hash_tools.domain.checksum;

public record Checksum(
    Algorithm algorithm,
    String value
) {
}
