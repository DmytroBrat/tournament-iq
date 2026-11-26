package com.artisyn.tournamentiq.match.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam", "field"})
    List<MatchEntity> findByTournamentIdOrderByStartsAtAscIdAsc(UUID tournamentId);
}
