package hashtools.core.strategy.formatter;

import java.util.stream.Stream;

public interface HeaderFormatter extends Formatter<String[], String[]> {

    default int getGreatestLength(String[] strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }
}
