# lp

Uses Java 24, Gradle build tool, JUnit for unit tests

## Running the tests

Run the command: `./gradlew clean test`

## Running the code

Run the command: `./gradlew run`

## Assumptions

- input file is complete and the data is valid
- taps are received in order, and in daily batches which we can window (this allows us to efficiently determine incomplete trips)

## Design considerations

- program to an interface (WindowedDataSource, DataSink, RecordProcessor), to allow for swappable/flexible source and sink (ie. something other than csv files)
- the TripChargeCalculator is a naive implementation and will need work to scale for more stops, journeys, etc (routes could be stored in a database)
- probably should have used opencsv library or similar instead of handrolling my own solution