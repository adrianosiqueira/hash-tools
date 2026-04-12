package hashtools.core.strategy.formatter;

public class RightAlignmentHeaderFormatter implements HeaderFormatter {

    private final char spacer;



    public RightAlignmentHeaderFormatter(char spacer) {
        this.spacer = spacer;
    }



    @Override
    public String[] format(String[] strings) {
        int desiredLength = this.getGreatestLength(strings);

        String[] resultStrings = new String[strings.length];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            result.setLength(0);
            result.append(strings[i]);

            while (result.length() < desiredLength) {
                result.insert(0, spacer);
            }

            resultStrings[i] = result.toString();
        }

        return resultStrings;
    }
}
