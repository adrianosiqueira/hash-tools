package hashtools.strategy.inputsource;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.result.ExceptionResult;

import java.util.Collection;
import java.util.Optional;

public class TextInputSource implements InputSource {

    private String text;



    public TextInputSource(String text) {
        this.text = text;
    }



    @Override
    public Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        try {
            byte[] bytes = text.getBytes();
            generators.forEach(messageDigest -> messageDigest.receiveBytes(bytes));
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
}
