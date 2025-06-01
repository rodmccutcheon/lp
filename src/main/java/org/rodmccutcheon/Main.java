package org.rodmccutcheon;

import org.rodmccutcheon.processor.TapsToTripsRecordProcessor;
import org.rodmccutcheon.sink.CsvDataSink;
import org.rodmccutcheon.model.Tap;
import org.rodmccutcheon.model.Trip;
import org.rodmccutcheon.source.CsvWindowedDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@ComponentScan("org.rodmccutcheon")
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        CsvWindowedDataSource windowedDataSource = context.getBean(CsvWindowedDataSource.class);
        TapsToTripsRecordProcessor tapsToTripsRecordProcessor = context.getBean(TapsToTripsRecordProcessor.class);
        CsvDataSink csvDataSink = context.getBean(CsvDataSink.class);

        Stream<List<Tap>> tapBatches = windowedDataSource.stream();

        List<Trip> allTrips = tapBatches
                .flatMap(tapBatch -> tapsToTripsRecordProcessor.process(tapBatch).stream())
                .toList();

        csvDataSink.write("trips.csv", allTrips);
    }
}
