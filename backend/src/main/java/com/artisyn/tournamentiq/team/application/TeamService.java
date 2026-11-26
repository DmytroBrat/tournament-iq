package com.artisyn.tournamentiq.team.application;

import com.artisyn.tournamentiq.team.persistence.TeamEntity;
import com.artisyn.tournamentiq.team.persistence.TeamRepository;
import com.artisyn.tournamentiq.tournament.application.TournamentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teams;
    private final TournamentService tournaments;

    public TeamService(TeamRepository teams, TournamentService tournaments) {
        this.teams = teams;
        this.tournaments = tournaments;
    }

    public List<TeamSummary> list(UUID tournamentId) {
        tournaments.requireExists(tournamentId);
        return teams.findByTournamentIdOrderByNameAscIdAsc(tournamentId).stream()
                .map(TeamSummary::from)
                .toList();
    }

    @Transactional
    public TeamSummary register(UUID tournamentId, String name) {
        tournaments.requireExists(tournamentId);
        return TeamSummary.from(teams.save(new TeamEntity(tournamentId, name.strip())));
    }

    public record TeamSummary(UUID id, String name) {
        private static TeamSummary from(TeamEntity entity) {
            return new TeamSummary(entity.getId(), entity.getName());
        }
    }
}
