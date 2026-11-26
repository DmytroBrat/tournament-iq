package com.artisyn.tournamentiq.match.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchAssignmentTest {
    private static final UUID HOME = UUID.fromString("20000000-0000-4000-8000-000000000001");
    private static final UUID AWAY = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID FIELD = UUID.fromString("30000000-0000-4000-8000-000000000001");
    private static final Instant START = Instant.parse("2026-11-28T09:00:00Z");

    @Test
    void acceptsDistinctTeamsAndAForwardTimeRange() {
        MatchAssignment assignment = new MatchAssignment(HOME, AWAY, FIELD, START, START.plusSeconds(3600));
        assertThat(assignment.endsAt()).isAfter(assignment.startsAt());
    }

    @Test
    void rejectsTheSameTeamOnBothSides() {
        assertThatExceptionOfType(InvalidMatchException.class)
                .isThrownBy(() -> new MatchAssignment(HOME, HOME, FIELD, START, START.plusSeconds(3600)))
                .withMessage("Choose two different teams.");
    }

    @Test
    void rejectsAnEndAtOrBeforeTheStoredStartTime() {
        for (Instant end : new Instant[]{START.minusSeconds(1), START, START.plusNanos(1)}) {
            assertThatThrownBy(() -> new MatchAssignment(HOME, AWAY, FIELD, START, end))
                    .isInstanceOf(InvalidMatchException.class)
                    .hasMessage("End time must be after start time.");
        }
    }
}
