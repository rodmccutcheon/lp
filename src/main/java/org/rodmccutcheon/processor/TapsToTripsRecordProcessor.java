package org.rodmccutcheon.processor;

import org.rodmccutcheon.model.Status;
import org.rodmccutcheon.model.Tap;
import org.rodmccutcheon.model.TapType;
import org.rodmccutcheon.model.Trip;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TapsToTripsRecordProcessor implements RecordProcessor<Tap, Trip> {

    private final Map<Long, Tap> pendingTaps = new HashMap<>();
    private final TripChargeCalculator tripChargeCalculator = new TripChargeCalculator();

    @Override
    public List<Trip> process(List<Tap> taps) {
        List<Trip> trips = new ArrayList<>();

        taps.forEach(tap -> {
            if (tap.tapType().equals(TapType.ON)) {
                pendingTaps.put(tap.pan(), tap);
            } else if (tap.tapType().equals(TapType.OFF)) {
                Tap tapOff = tap;
                Tap tapOn = pendingTaps.remove(tapOff.pan());
                if (tapOn != null) {
                    if (tapOn.stopId().equals(tapOff.stopId())) {
                        Trip trip = new Trip(tapOn.dateTimeUtc(), tapOff.dateTimeUtc(), Duration.between(tapOn.dateTimeUtc(), tapOff.dateTimeUtc()).getSeconds(), tapOn.stopId(), tapOff.stopId(), 0.0, tapOn.companyId(), tapOn.busId(), tapOn.pan(), Status.CANCELLED);
                        trips.add(trip);
                    } else {
                        Double tripCharge = tripChargeCalculator.calculateTripCharge(tapOn.stopId(), tapOff.stopId());
                        Trip trip = new Trip(tapOn.dateTimeUtc(), tapOff.dateTimeUtc(), Duration.between(tapOn.dateTimeUtc(), tapOff.dateTimeUtc()).getSeconds(), tapOn.stopId(), tapOff.stopId(), tripCharge, tapOn.companyId(), tapOn.busId(), tapOn.pan(), Status.COMPLETED);
                        trips.add(trip);
                    }
                }
            }
        });

        // Any remaining pending taps indicate incomplete trip/s
        pendingTaps.forEach((pan, tap) -> {
            Trip trip = new Trip(tap.dateTimeUtc(), null, null, tap.stopId(), null, tripChargeCalculator.calculateMaximumFare(tap.stopId()), tap.companyId(), tap.busId(), tap.pan(), Status.INCOMPLETE);
            trips.add(trip);
        });

        return trips;
    }
}
