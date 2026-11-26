package com.artisyn.tournamentiq.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<TeamEntity, UUID> {
    List<TeamEntity> findByTournamentIdOrderByNameAscIdAsc(UUID tournamentId);

    Optional<TeamEntity> findByTournamentIdAndId(UUID tournamentId, UUID id);
}
