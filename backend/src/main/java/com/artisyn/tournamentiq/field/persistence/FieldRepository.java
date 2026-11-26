package com.artisyn.tournamentiq.field.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<FieldEntity, UUID> {
    List<FieldEntity> findByTournamentIdOrderByNameAscIdAsc(UUID tournamentId);

    Optional<FieldEntity> findByTournamentIdAndId(UUID tournamentId, UUID id);
}
