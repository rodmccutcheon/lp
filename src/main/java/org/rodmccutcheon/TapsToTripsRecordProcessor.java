package org.rodmccutcheon;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TapsToTripsRecordProcessor implements RecordProcessor<Tap, Trip> {

    private final Map<Long, Tap> pendingTaps = new HashMap<>();

    @Override
    public List<Trip> process(List<Tap> taps) {
        List<Trip> trips = new ArrayList<>();

        taps.forEach(tap -> {
            if (tap.tapType().equals(TapType.ON)) {
                pendingTaps.put(tap.pan(), tap);
            } else if (tap.tapType().equals(TapType.OFF)) {
                Tap tapOff = tap;
                Tap tapOn = pendingTaps.remove(tap.pan());
                if (tapOn != null) {
                    Double tripCharge = 0.0;
                    if (tapOn.stopId().equals(tapOff.stopId())) {
                        Trip trip = new Trip(tapOn.dateTimeUtc(), tapOff.dateTimeUtc(), Duration.between(tapOn.dateTimeUtc(), tapOff.dateTimeUtc()).getSeconds(), tapOn.stopId(), tapOff.stopId(), tripCharge, tapOn.companyId(), tapOn.busId(), tapOn.pan(), Status.CANCELLED);
                        trips.add(trip);
                    }
                }
            }
        });

        return trips;
    }
}
