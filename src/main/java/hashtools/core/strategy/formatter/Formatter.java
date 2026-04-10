package hashtools.core.strategy.formatter;

import java.util.stream.Stream;

public interface Formatter<T> {

    String format(T t);



    default int getGreatestLength(String... strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }

    default String[] alignToRight(String... strings) {
        int length = this.getGreatestLength(strings);

        return Stream
            .of(strings)
            .map(string -> String.format("%" + length + "s", string))
            .toArray(String[]::new);
    }
}
