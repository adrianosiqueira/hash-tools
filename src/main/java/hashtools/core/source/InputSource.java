package hashtools.core.source;

import hashtools.core.strategy.identification.InputIdentification;
import hashtools.core.strategy.identification.TextInputIdentification;
import hashtools.core.strategy.messagedigest.MessageDigestUpdate;
import hashtools.core.strategy.problem.FileInputProblemDetection;
import hashtools.core.strategy.problem.ProblemDetection;
import hashtools.core.strategy.problem.TextInputProblemDetection;

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
            _ -> {},
            () -> "",
            new FileInputProblemDetection(filePath)
        );
    }

    public static InputSource textInputSource(String text) {
        return new InputSource(
            _ -> {},
            new TextInputIdentification(text),
            new TextInputProblemDetection(text)
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
}
