package org.rodmccutcheon;

import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        CsvWindowedDataSource windowedDataSource = new CsvWindowedDataSource();

        TapsToTripsRecordProcessor tapsToTripsRecordProcessor = new TapsToTripsRecordProcessor();

        Stream<List<Tap>> tapBatches = windowedDataSource.stream();

        List<Trip> allTrips = tapBatches
                .flatMap(tapBatch -> tapsToTripsRecordProcessor.process(tapBatch).stream())
                .toList();

        CsvDataSink csvDataSink = new CsvDataSink();
        csvDataSink.write("trips.csv", allTrips);
    }
}
