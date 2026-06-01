package hashtools.core.strategy.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeftAlignmentHeaderFormatter extends HeaderFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeftAlignmentHeaderFormatter.class);



    private final char spacer;



    public LeftAlignmentHeaderFormatter(char spacer) {
        this.spacer = spacer;
    }



    @Override
    public String[] format(String[] strings) {
        int desiredLength = this.getGreatestLength(strings);
        LOGGER.debug("Aligning headers to '{}' characters.", desiredLength);

        String[] resultStrings = new String[strings.length];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            result.setLength(0);
            result.append(strings[i]);

            while (result.length() < desiredLength) {
                result.append(spacer);
            }

            resultStrings[i] = result.toString();
        }

        return resultStrings;
    }
}
