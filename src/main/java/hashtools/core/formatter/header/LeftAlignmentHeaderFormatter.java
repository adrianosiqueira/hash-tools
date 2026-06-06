package hashtools.core.formatter.header;

public class LeftAlignmentHeaderFormatter extends HeaderFormatter {

    @Override
    public String[] align(String[] headers, int desiredLength, char spacer) {
        String[] resultStrings = new String[headers.length];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < headers.length; i++) {
            result.setLength(0);
            result.append(headers[i]);

            while (result.length() < desiredLength) {
                result.append(spacer);
            }

            resultStrings[i] = result.toString();
        }

        return resultStrings;
    }
}
