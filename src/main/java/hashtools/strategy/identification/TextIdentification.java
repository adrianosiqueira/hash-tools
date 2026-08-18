package hashtools.strategy.identification;

public class TextIdentification implements Identification {

    private String text;



    public TextIdentification(String text) {
        this.text = text;
    }



    @Override
    public String identify() {
        return text;
    }
}
