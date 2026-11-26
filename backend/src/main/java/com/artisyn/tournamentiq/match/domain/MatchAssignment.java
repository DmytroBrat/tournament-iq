package com.artisyn.tournamentiq.match.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record MatchAssignment(
        UUID homeTeamId,
        UUID awayTeamId,
        UUID fieldId,
        Instant startsAt,
        Instant endsAt
) {
    public MatchAssignment {
        if (homeTeamId.equals(awayTeamId)) {
            throw new InvalidMatchException("awayTeamId", "Choose two different teams.");
        }

        // Compare the values at the precision PostgreSQL stores.
        startsAt = startsAt.truncatedTo(ChronoUnit.MICROS);
        endsAt = endsAt.truncatedTo(ChronoUnit.MICROS);
        if (!endsAt.isAfter(startsAt)) {
            throw new InvalidMatchException("endsAt", "End time must be after start time.");
        }
    }
}
