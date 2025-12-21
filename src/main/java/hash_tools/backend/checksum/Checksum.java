package hash_tools.backend.checksum;

import java.util.Optional;

public class Checksum {

    private Algorithm algorithm;
    private String value;
    private boolean valid;



    private Checksum(Algorithm algorithm, String value, boolean valid) {
        this.algorithm = algorithm;
        this.value = value;
        this.valid = valid;
    }



    public static Checksum fromValue(String value) {
        return Optional
            .ofNullable(value)
            .map(String::length)
            .flatMap(Algorithm::fromLength)
            .map(algorithm -> new Checksum(algorithm, value, true))
            .orElse(new Checksum(null, null, false));
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



    public Algorithm algorithm() {
        return algorithm;
    }

    public String value() {
        return value;
    }

    public boolean valid() {
        return valid;
    }
}
