package org.rodmccutcheon;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CsvWindowedDataSource implements WindowedDataSource<Tap> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

    @Override
    public Stream<List<Tap>> stream() {

        Map<ZonedDateTime, List<Tap>> batchedTaps = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream("src/main/resources/taps.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            reader.readLine(); // skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                long id = Long.parseLong(parts[0].trim());
                ZonedDateTime dateTimeUtc = ZonedDateTime.parse(parts[1].trim(), dateTimeFormatter);
                TapType tapType = TapType.valueOf(parts[2].trim());
                String stopId = parts[3].trim();
                String companyId = parts[4].trim();
                String busId = parts[5].trim();
                long pan = Long.parseLong(parts[6].trim());
                Tap tap = new Tap(id, dateTimeUtc, tapType, stopId, companyId, busId, pan);
                batchedTaps.computeIfAbsent(dateTimeUtc, k -> new ArrayList<>()).add(tap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return batchedTaps.values().stream();
    }
}
