package hashtools.strategy.problemdetection;

public class ChecksumTextProblemDetection implements ProblemDetection {

    private String text;



    public ChecksumTextProblemDetection(String text) {
        this.text = text;
    }
}
