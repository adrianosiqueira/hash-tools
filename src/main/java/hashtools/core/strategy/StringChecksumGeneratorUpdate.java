package hashtools.core.strategy;

import hashtools.core.domain.ChecksumGenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class StringChecksumGeneratorUpdate implements ChecksumGeneratorUpdate {

    private String string;



    public StringChecksumGeneratorUpdate(String string) {
        this.string = Objects.requireNonNull(string);
    }



    @Override
    public void update(Collection<ChecksumGenerator> generators)
    throws InterruptedException, IOException {
        var bytes = string.getBytes();

        for (var generator : generators) {
            generator.update(bytes);
        }
    }
}
