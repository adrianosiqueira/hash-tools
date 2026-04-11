package hashtools.core.strategy.formatter;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Formatter {

    default int getGreatestLength(String... strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }

    default String[] alignToLeft(char character, String... strings) {
        return this.align(
            result -> result.append(character),
            strings
        );
    }

    default String[] alignToRight(char character, String... strings) {
        return this.align(
            result -> result.insert(0, character),
            strings
        );
    }

    default String[] align(Consumer<StringBuilder> correction, String... strings) {
        int desiredLength = this.getGreatestLength(strings);

        String[] resultStrings = new String[strings.length];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            result.setLength(0);
            result.append(strings[i]);

            while (result.length() < desiredLength) {
                correction.accept(result);
            }

            resultStrings[i] = result.toString();
        }

        return resultStrings;
    }
}
