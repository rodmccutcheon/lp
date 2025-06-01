package org.rodmccutcheon;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CsvDataSink implements DataSink<Trip> {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

    String[] headers = { "Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount", "CompanyId", "BusID", "PAN", "Status" };

    @Override
    public void write(String filename, List<Trip> trips) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write(String.join(",", headers));
            writer.newLine();

            StringBuilder stringBuilder = new StringBuilder();
            for (Trip trip : trips) {
                stringBuilder.setLength(0);
                stringBuilder.append(trip.started().format(dateTimeFormatter)).append(",")
                        .append(trip.finished() != null ? trip.finished().format(dateTimeFormatter) : "").append(",")
                        .append(trip.durationSecs() != null ? trip.durationSecs() : "").append(",")
                        .append(trip.fromStopId()).append(",")
                        .append(trip.toStopId() != null ? trip.toStopId() : "").append(",")
                        .append(trip.chargeAmount()).append(",")
                        .append(trip.companyId()).append(",")
                        .append(trip.busId()).append(",")
                        .append(trip.pan()).append(",")
                        .append(trip.status());

                writer.write(stringBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
