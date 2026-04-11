package hashtools.core.strategy.formatter;

import java.util.stream.Stream;

public interface Formatter {

    default int getGreatestLength(String... strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }

    default String[] alignToRight(char character, String... strings) {
        int desiredLength = this.getGreatestLength(strings);

        String[] resultStrings = new String[strings.length];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            result.setLength(0);
            result.append(strings[i]);

            while (result.length() < desiredLength) {
                result.insert(0, character);
            }

            resultStrings[i] = result.toString();
        }

        return resultStrings;
    }
}
