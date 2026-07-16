package hashtools.domain.checksum;

import java.util.Objects;

public class CheckerChecksum {

    private Checksum official;
    private Checksum generated;



    public CheckerChecksum() {
        this.official = new Checksum();
        this.generated = new Checksum();
    }



    public boolean matches() {
        return official.matches(generated);
    }



    public void setOfficial(Checksum official) {
        this.official = Objects.requireNonNull(official);
    }

    public void setGenerated(Checksum generated) {
        this.generated = Objects.requireNonNull(generated);
    }

    @Override
    public String toString() {
        return "CheckerChecksum{" +
            "official=" + official +
            ", generated=" + generated +
            ", matches=" + this.matches() +
            '}';
    }
}
