package com.artisyn.tournamentiq.match.application;

import com.artisyn.tournamentiq.field.persistence.FieldEntity;
import com.artisyn.tournamentiq.field.persistence.FieldRepository;
import com.artisyn.tournamentiq.match.domain.InvalidMatchException;
import com.artisyn.tournamentiq.match.domain.MatchAssignment;
import com.artisyn.tournamentiq.match.persistence.MatchEntity;
import com.artisyn.tournamentiq.match.persistence.MatchRepository;
import com.artisyn.tournamentiq.team.persistence.TeamEntity;
import com.artisyn.tournamentiq.team.persistence.TeamRepository;
import com.artisyn.tournamentiq.tournament.application.TournamentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matches;
    private final TournamentService tournaments;
    private final TeamRepository teams;
    private final FieldRepository fields;

    public MatchService(MatchRepository matches, TournamentService tournaments, TeamRepository teams,
                        FieldRepository fields) {
        this.matches = matches;
        this.tournaments = tournaments;
        this.teams = teams;
        this.fields = fields;
    }

    public List<ScheduledMatch> schedule(UUID tournamentId) {
        tournaments.requireExists(tournamentId);
        return matches.findByTournamentIdOrderByStartsAtAscIdAsc(tournamentId).stream()
                .map(ScheduledMatch::from)
                .toList();
    }

    @Transactional
    public ScheduledMatch create(UUID tournamentId, MatchAssignment assignment) {
        tournaments.requireExists(tournamentId);
        TeamEntity home = requireTeam(tournamentId, assignment.homeTeamId(), "homeTeamId");
        TeamEntity away = requireTeam(tournamentId, assignment.awayTeamId(), "awayTeamId");
        FieldEntity field = fields.findByTournamentIdAndId(tournamentId, assignment.fieldId())
                .orElseThrow(() -> new InvalidMatchException("fieldId", "Choose a field in this tournament."));

        MatchEntity match = new MatchEntity(tournamentId, home, away, field, assignment.startsAt(), assignment.endsAt());
        return ScheduledMatch.from(matches.save(match));
    }

    private TeamEntity requireTeam(UUID tournamentId, UUID teamId, String field) {
        return teams.findByTournamentIdAndId(tournamentId, teamId)
                .orElseThrow(() -> new InvalidMatchException(field, "Choose a team in this tournament."));
    }

    public record ScheduledMatch(UUID id, Participant homeTeam, Participant awayTeam, Participant field,
                                 Instant startsAt, Instant endsAt) {
        private static ScheduledMatch from(MatchEntity entity) {
            return new ScheduledMatch(
                    entity.getId(),
                    new Participant(entity.getHomeTeam().getId(), entity.getHomeTeam().getName()),
                    new Participant(entity.getAwayTeam().getId(), entity.getAwayTeam().getName()),
                    new Participant(entity.getField().getId(), entity.getField().getName()),
                    entity.getStartsAt(),
                    entity.getEndsAt()
            );
        }
    }

    public record Participant(UUID id, String name) {}
}
