package com.artisyn.tournamentiq.tournament.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {
    List<TournamentEntity> findAllByOrderByNameAscIdAsc();
}
