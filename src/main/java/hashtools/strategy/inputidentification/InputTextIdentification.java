package hashtools.strategy.inputidentification;

public class InputTextIdentification implements InputIdentification {

    private String text;



    public InputTextIdentification(String text) {
        this.text = text;
    }



    @Override
    public String getIdentification() {
        return text;
    }
}
