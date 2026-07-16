package hashtools.strategy.inputsource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;

public class TextInputSource implements InputSource {

    private String text;



    public TextInputSource(String text) {
        this.text = text;
    }



    @Override
    public void updateMessageDigest(Collection<MessageDigest> messageDigests) throws IOException {
        byte[] bytes = text.getBytes();
        messageDigests.forEach(messageDigest -> messageDigest.update(bytes));
    }

    @Override
    public String getIdentification() {
        return text;
    }

    @Override
    public Optional<String> detectProblem() {
        if (text == null) {
            return Optional.of("The text is null");
        }

        return Optional.empty();
    }
}
