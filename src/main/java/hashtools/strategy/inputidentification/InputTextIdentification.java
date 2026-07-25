package hashtools.strategy.inputidentification;

public record InputTextIdentification(
    String text
) implements InputIdentification {

    @Override
    public String getIdentification() {
        return text;
    }
}
