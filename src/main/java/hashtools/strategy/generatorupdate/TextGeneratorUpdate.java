package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.commom.Result;

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
        progressConsumer.accept(0.0);

        generators.forEach(generator -> generator.receiveBytes(bytes));

        progressConsumer.accept(1.0);
    }

    @Override
    public Result<Void, String> updateGenerators(Collection<ChecksumGenerator> generators, Consumer<Double> progressTracker) {
        try {
            var bytes = text.getBytes();
            progressTracker.accept(0.0);

            for (var generator : generators) {
                generator.receiveBytes(bytes);
            }

            return Result.ok(null);
        } catch (Exception e) {
            return Result.error("Failed to update the generators");
        } finally {
            progressTracker.accept(1.0);
        }
    }
}
