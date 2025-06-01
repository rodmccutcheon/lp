package org.rodmccutcheon;

import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class CsvWindowedDataSource implements WindowedDataSource<Tap> {

    private final TapParser tapParser;

    public CsvWindowedDataSource(TapParser tapParser) {
        this.tapParser = tapParser;
    }

    @Override
    public Stream<List<Tap>> stream() {

        Map<LocalDate, List<Tap>> batchedTaps = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream("src/main/resources/taps.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            reader.readLine(); // skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Tap tap = tapParser.parseTap(parts);
                batchedTaps.computeIfAbsent(tap.dateTimeUtc().toLocalDate(), k -> new ArrayList<>()).add(tap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return batchedTaps.values().stream();
    }
}
