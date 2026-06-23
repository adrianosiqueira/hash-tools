package hashtools.core.source;

import hashtools.core.strategy.identification.FileInputIdentification;
import hashtools.core.strategy.identification.InputIdentification;
import hashtools.core.strategy.identification.TextInputIdentification;
import hashtools.core.strategy.messagedigest.FileMessageDigestUpdate;
import hashtools.core.strategy.messagedigest.MessageDigestUpdate;
import hashtools.core.strategy.messagedigest.TextMessageDigestUpdate;
import hashtools.core.strategy.problem.FileInputProblemDetection;
import hashtools.core.strategy.problem.ProblemDetection;
import hashtools.core.strategy.problem.TextInputProblemDetection;

import java.security.MessageDigest;
import java.util.Optional;

public class InputSource {

    private MessageDigestUpdate messageDigestUpdate;
    private InputIdentification inputIdentification;
    private ProblemDetection problemDetection;



    private InputSource(MessageDigestUpdate messageDigestUpdate, InputIdentification inputIdentification, ProblemDetection problemDetection) {
        this.messageDigestUpdate = messageDigestUpdate;
        this.inputIdentification = inputIdentification;
        this.problemDetection = problemDetection;
    }



    public static InputSource fileInputSource(String filePath) {
        return new InputSource(
            new FileMessageDigestUpdate(filePath),
            new FileInputIdentification(filePath),
            new FileInputProblemDetection(filePath)
        );
    }

    public static InputSource textInputSource(String text) {
        return new InputSource(
            new TextMessageDigestUpdate(text),
            new TextInputIdentification(text),
            new TextInputProblemDetection(text)
        );
    }

    public static InputSource nullInputSource() {
        return new InputSource(
            _ -> {},
            () -> "",
            Optional::empty
        );
    }



    public MessageDigestUpdate getMessageDigestUpdate() {
        return messageDigestUpdate;
    }

    public InputIdentification getInputIdentification() {
        return inputIdentification;
    }

    public ProblemDetection getProblemDetection() {
        return problemDetection;
    }



    public void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
        messageDigestUpdate.update(messageDigest);
    }

    public String identify() {
        return inputIdentification.identify();
    }

    public Optional<String> detectProblem() {
        return problemDetection.detect();
    }
}
