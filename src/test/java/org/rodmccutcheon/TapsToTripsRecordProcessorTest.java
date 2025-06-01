package org.rodmccutcheon;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TapsToTripsRecordProcessorTest {

    @Test
    void singleCancelledTrip() {
        Tap tapOn = new Tap(1, ZonedDateTime.now().minusSeconds(30), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        Tap tapOff = new Tap(2, ZonedDateTime.now(), TapType.OFF, "Stop1", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = Arrays.asList(tapOn, tapOff);

        TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

        List<Trip> trips = processor.process(taps);
        assertEquals(1, trips.size());
        assertEquals(0.0, trips.getFirst().chargeAmount());
        assertEquals(Status.CANCELLED, trips.getFirst().status());
    }

    @Test
    void singleCompletedTrip() {
        Tap tapOn = new Tap(1, ZonedDateTime.now().minusMinutes(10), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        Tap tapOff = new Tap(2, ZonedDateTime.now(), TapType.OFF, "Stop2", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = Arrays.asList(tapOn, tapOff);
        TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        assertEquals(3.25, trips.getFirst().chargeAmount());
        assertEquals(Status.COMPLETED, trips.getFirst().status());
    }

    @Test
    void singleCompletedTripInReverse() {
        Tap tapOn = new Tap(1, ZonedDateTime.now().minusMinutes(10), TapType.ON, "Stop2", "Company1", "Bus37", 5500005555555559L);
        Tap tapOff = new Tap(2, ZonedDateTime.now(), TapType.OFF, "Stop1", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = Arrays.asList(tapOn, tapOff);
        TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        assertEquals(3.25, trips.getFirst().chargeAmount());
        assertEquals(Status.COMPLETED, trips.getFirst().status());
    }

    @Test
    void singleIncompleteTripFromStop1() {
        Tap tapOn = new Tap(1, ZonedDateTime.now().minusMinutes(10), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = List.of(tapOn);
        TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        assertEquals(7.30, trips.getFirst().chargeAmount());
        assertEquals(Status.INCOMPLETE, trips.getFirst().status());
    }

    @Test
    void singleIncompleteTripFromStop2() {
        Tap tapOn = new Tap(1, ZonedDateTime.now().minusMinutes(10), TapType.ON, "Stop2", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = List.of(tapOn);
        TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        assertEquals(5.50, trips.getFirst().chargeAmount());
        assertEquals(Status.INCOMPLETE, trips.getFirst().status());
    }

}
