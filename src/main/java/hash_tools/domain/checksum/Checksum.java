package hash_tools.domain.checksum;

public record Checksum(
    Algorithm algorithm,
    String value
) {

    public static Checksum fromValue(String value) {
        if (value == null) {
            return new Checksum(null, null);
        }


        Algorithm algorithm = Algorithm
            .fromLength(value.length())
            .orElse(null);


        return new Checksum(algorithm, value);
    }



    public boolean matches(Checksum other) {
        if (other == null) {
            return false;
        } else if (algorithm != other.algorithm) {
            return false;
        } else if (value == null) {
            return other.value == null;
        } else {
            return value.equalsIgnoreCase(other.value);
        }
    }

    public boolean valid() {
        return valid;
    }
}
