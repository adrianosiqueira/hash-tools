package hashtools.core.formatter.header;

import java.util.stream.Stream;

public class HeaderFormatter {

    public String[] alignToLeft(String[] headers) {
        String[] alignedHeaders = new String[headers.length];
        int desiredLength = this.getGreatestLength(headers);

        for (int i = 0; i < headers.length; i++) {
            StringBuilder header = new StringBuilder();
            header.append(headers[i]);

            while (header.length() < desiredLength) {
                header.append(this.fill());
            }

            alignedHeaders[i] = header.toString();
        }

        return alignedHeaders;
    }



    private int getGreatestLength(String[] strings) {
        return Stream
            .of(strings)
            .map(String::length)
            .reduce(Integer::max)
            .orElse(0);
    }

    private char fill() {
        return '.';
    }
}
