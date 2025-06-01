package org.rodmccutcheon;

import java.time.ZonedDateTime;

public record Trip(ZonedDateTime started,
                   ZonedDateTime finished,
                   Long durationSecs,
                   String fromStopId,
                   String toStopId,
                   Double chargeAmount,
                   String companyId,
                   String busId,
                   Long pan,
                   Status status) {
}
