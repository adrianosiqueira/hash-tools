package hashtools.strategy.checksumgeneratorupdater;

import hashtools.domain.algorithm.ChecksumGenerator;

import java.util.Collection;

public class TextChecksumGeneratorUpdate implements ChecksumGeneratorUpdate {

    private String text;



    public TextChecksumGeneratorUpdate(String text) {
        this.text = text;
    }



    @Override
    public Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        byte[] bytes = text.getBytes();

        generators.forEach(generator -> generator.receiveBytes(
            bytes,
            bytes.length
        ));

        return new Result.Success();
    }

    @Override
    public void cancelChecksumGeneratorsUpdate() {
    }
}
