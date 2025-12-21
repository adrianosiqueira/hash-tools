package hash_tools.backend.result;

import hash_tools.backend.checksum.Checksum;

import java.util.function.Consumer;

public class ComparatorResult {

    private Checksum checksum1;
    private String identification1;

    private Checksum checksum2;
    private String identification2;



    public ComparatorResult(Checksum checksum1, String identification1, Checksum checksum2, String identification2) {
        this.checksum1 = checksum1;
        this.identification1 = identification1;
        this.checksum2 = checksum2;
        this.identification2 = identification2;
    }



    public ComparatorResult consume(Consumer<ComparatorResult> consumer) {
        consumer.accept(this);
        return this;
    }



    public Checksum checksum1() {
        return checksum1;
    }

    public String identification1() {
        return identification1;
    }

    public Checksum checksum2() {
        return checksum2;
    }

    public String identification2() {
        return identification2;
    }
}
