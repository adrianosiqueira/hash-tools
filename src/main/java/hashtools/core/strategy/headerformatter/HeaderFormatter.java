package hashtools.core.strategy.headerformatter;

import java.util.stream.Stream;

public abstract class HeaderFormatter {


    public String[] format(String[] headers, char spacer) {
        return this.align(
            headers,
            this.getGreatestLength(headers),
            spacer
        );
    }



    protected abstract String[] align(String[] headers, int desiredLength, char spacer);



    private int getGreatestLength(String[] strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }
}
