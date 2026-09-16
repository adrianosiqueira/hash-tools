package hashtools.strategy.generatorupdate;

import hashtools.domain.checksum.ChecksumGenerator;
import hashtools.domain.commom.Result;

import java.util.Collection;
import java.util.function.Consumer;

public class TextGeneratorUpdate implements GeneratorUpdate {

    private String text;



    public TextGeneratorUpdate(String text) {
        this.text = text;
    }



    @Override
    public Result<Void, String> updateGenerators(Collection<ChecksumGenerator> generators, Consumer<Double> progressTracker) {
        try {
            var bytes = text.getBytes();
            progressTracker.accept(0.0);

            for (var generator : generators) {
                generator.receiveBytes(bytes);
            }

            return new Result.Ok<>(null);
        } catch (Exception e) {
            return new Result.Error<>("Failed to update the generators");
        } finally {
            progressTracker.accept(1.0);
        }
    }
}
