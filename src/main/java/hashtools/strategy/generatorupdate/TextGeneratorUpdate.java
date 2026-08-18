package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;

import java.util.Collection;
import java.util.function.Consumer;

public class TextGeneratorUpdate implements GeneratorUpdate {

    private String text;



    public TextGeneratorUpdate(String text) {
        this.text = text;
    }



    @Override
    public void update(Collection<ChecksumGenerator> generators, Consumer<Double> progressConsumer) throws RuntimeException {
        byte[] bytes = text.getBytes();

        generators.forEach(generator -> generator.receiveBytes(bytes));
    }
}
