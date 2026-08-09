package hashtools.strategy.inputsource;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class TextInputSource implements InputSource {

    private String text;
    private Consumer<Double> progressTracking;



    public TextInputSource(String text) {
        this.text = text;
        this.progressTracking = _ -> {};
    }



    @Override
    public Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        if (generators.isEmpty()) {
            return new CanceledResult();
        }



        try {
            byte[] bytes = text.getBytes();
            generators.forEach(messageDigest -> messageDigest.receiveBytes(bytes));
            progressTracking.accept(1.0);
        } catch (Exception e) {
            return new ExceptionResult(e);
        }

        return new SuccessResult();
    }

    @Override
    public String getIdentification() {
        return text;
    }

    @Override
    public Optional<String> detectProblem() {
        if (text == null) {
            return Optional.of("The text is null");
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setProgressTracking(Consumer<Double> tracking) {
        this.progressTracking = Objects.requireNonNull(tracking);
    }
}
