package org.rodmccutcheon;

import java.time.ZonedDateTime;

public record Tap(long id, ZonedDateTime dateTimeUtc, TapType tapType, String stopId, String companyId, String busId, String pan) {
}
