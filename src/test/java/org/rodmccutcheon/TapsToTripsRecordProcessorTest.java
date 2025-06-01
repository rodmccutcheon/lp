package org.rodmccutcheon;

import org.junit.jupiter.api.Test;
import org.rodmccutcheon.model.Status;
import org.rodmccutcheon.model.Tap;
import org.rodmccutcheon.model.TapType;
import org.rodmccutcheon.model.Trip;
import org.rodmccutcheon.processor.TapsToTripsRecordProcessor;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TapsToTripsRecordProcessorTest {

    TapsToTripsRecordProcessor processor = new TapsToTripsRecordProcessor();

    @Test
    void cancelledTrip() {
        ZonedDateTime now = ZonedDateTime.now();
        Tap tapOn = new Tap(1, now.minusSeconds(30), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        Tap tapOff = new Tap(2, now, TapType.OFF, "Stop1", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = Arrays.asList(tapOn, tapOff);

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        Trip trip = trips.getFirst();
        assertEquals(now.minusSeconds(30), trip.started());
        assertEquals(now, trip.finished());
        assertEquals(30, trip.durationSecs());
        assertEquals("Stop1", trip.fromStopId());
        assertEquals("Stop1", trip.toStopId());
        assertEquals(0.0, trip.chargeAmount());
        assertEquals("Company1", trip.companyId());
        assertEquals("Bus37", trip.busId());
        assertEquals(5500005555555559L, trip.pan());
        assertEquals(Status.CANCELLED, trips.getFirst().status());
    }

    @Test
    void completedTrip() {
        ZonedDateTime now = ZonedDateTime.now();
        Tap tapOn = new Tap(1, now.minusMinutes(10), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        Tap tapOff = new Tap(2, now, TapType.OFF, "Stop2", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = Arrays.asList(tapOn, tapOff);

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        Trip trip = trips.getFirst();
        assertEquals(now.minusMinutes(10), trip.started());
        assertEquals(now, trip.finished());
        assertEquals(600, trip.durationSecs());
        assertEquals("Stop1", trip.fromStopId());
        assertEquals("Stop2", trip.toStopId());
        assertEquals(3.25, trip.chargeAmount());
        assertEquals("Company1", trip.companyId());
        assertEquals("Bus37", trip.busId());
        assertEquals(5500005555555559L, trip.pan());
        assertEquals(Status.COMPLETED, trips.getFirst().status());
    }

    @Test
    void incompleteTrip() {
        ZonedDateTime now = ZonedDateTime.now();
        Tap tapOn = new Tap(1, now.minusMinutes(10), TapType.ON, "Stop1", "Company1", "Bus37", 5500005555555559L);
        List<Tap> taps = List.of(tapOn);

        List<Trip> trips = processor.process(taps);

        assertEquals(1, trips.size());
        Trip trip = trips.getFirst();
        assertEquals(now.minusMinutes(10), trip.started());
        assertNull(trip.finished());
        assertNull(trip.durationSecs());
        assertEquals("Stop1", trip.fromStopId());
        assertNull(trip.toStopId());
        assertEquals(7.30, trip.chargeAmount());
        assertEquals("Company1", trip.companyId());
        assertEquals("Bus37", trip.busId());
        assertEquals(5500005555555559L, trip.pan());
        assertEquals(Status.INCOMPLETE, trips.getFirst().status());
    }

}
