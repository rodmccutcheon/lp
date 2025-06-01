package org.rodmccutcheon.source;

import org.rodmccutcheon.model.Tap;
import org.rodmccutcheon.model.TapType;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TapParser {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("UTC"));

    public Tap parseTap(String[] parts) {
        long id = Long.parseLong(parts[0].trim());
        ZonedDateTime dateTimeUtc = ZonedDateTime.parse(parts[1].trim(), dateTimeFormatter);
        TapType tapType = TapType.valueOf(parts[2].trim());
        String stopId = parts[3].trim();
        String companyId = parts[4].trim();
        String busId = parts[5].trim();
        long pan = Long.parseLong(parts[6].trim());
        return new Tap(id, dateTimeUtc, tapType, stopId, companyId, busId, pan);
    }
}
