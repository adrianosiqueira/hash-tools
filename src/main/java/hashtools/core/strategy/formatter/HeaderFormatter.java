package hashtools.core.strategy.formatter;

import java.util.stream.Stream;

public abstract class HeaderFormatter implements Formatter<String[], String[]> {

    protected int getGreatestLength(String[] strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }
}
