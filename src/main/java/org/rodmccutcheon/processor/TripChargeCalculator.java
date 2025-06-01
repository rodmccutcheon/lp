package org.rodmccutcheon.processor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TripChargeCalculator {

    private static final Map<Pair<String, String>, Double> FARE_TABLE;

    // Add routes in both order
    static {
        FARE_TABLE = new HashMap<>();
        FARE_TABLE.put(Pair.of("Stop1", "Stop2"), 3.25);
        FARE_TABLE.put(Pair.of("Stop2", "Stop1"), 3.25);
        FARE_TABLE.put(Pair.of("Stop2", "Stop3"), 5.50);
        FARE_TABLE.put(Pair.of("Stop3", "Stop2"), 5.50);
        FARE_TABLE.put(Pair.of("Stop1", "Stop3"), 7.30);
        FARE_TABLE.put(Pair.of("Stop3", "Stop1"), 7.30);
    }

    public Double calculateTripCharge(String fromStop, String toStop) {
        return FARE_TABLE.getOrDefault(Pair.of(fromStop, toStop), 0.0);
    }

    public Double calculateMaximumFare(String fromStop) {
        return switch (fromStop) {
            case "Stop1" -> 7.30;
            case "Stop2" -> 5.50;
            case "Stop3" -> 7.30;
            default -> 0.0;
        };
    }
}
