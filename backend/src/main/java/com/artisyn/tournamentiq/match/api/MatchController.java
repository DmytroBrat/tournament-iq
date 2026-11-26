package com.artisyn.tournamentiq.match.api;

import com.artisyn.tournamentiq.match.application.MatchService;
import com.artisyn.tournamentiq.match.application.MatchService.ScheduledMatch;
import com.artisyn.tournamentiq.match.domain.MatchAssignment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}")
class MatchController {
    private final MatchService matches;

    MatchController(MatchService matches) {
        this.matches = matches;
    }

    @GetMapping("/schedule")
    List<ScheduledMatch> schedule(@PathVariable UUID tournamentId) {
        return matches.schedule(tournamentId);
    }

    @PostMapping("/matches")
    @ResponseStatus(HttpStatus.CREATED)
    ScheduledMatch create(@PathVariable UUID tournamentId, @Valid @RequestBody CreateMatchRequest request) {
        return matches.create(tournamentId, request.toAssignment());
    }

    record CreateMatchRequest(
            @NotNull(message = "Choose a home team.") UUID homeTeamId,
            @NotNull(message = "Choose an away team.") UUID awayTeamId,
            @NotNull(message = "Choose a field.") UUID fieldId,
            @NotNull(message = "Enter a start time.") OffsetDateTime startsAt,
            @NotNull(message = "Enter an end time.") OffsetDateTime endsAt
    ) {
        MatchAssignment toAssignment() {
            return new MatchAssignment(homeTeamId, awayTeamId, fieldId, startsAt.toInstant(), endsAt.toInstant());
        }
    }
}
