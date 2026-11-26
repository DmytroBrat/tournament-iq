package com.artisyn.tournamentiq.tournament.application;

import com.artisyn.tournamentiq.shared.application.ResourceNotFoundException;
import com.artisyn.tournamentiq.tournament.persistence.TournamentEntity;
import com.artisyn.tournamentiq.tournament.persistence.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TournamentService {
    private final TournamentRepository tournaments;

    public TournamentService(TournamentRepository tournaments) {
        this.tournaments = tournaments;
    }

    public List<TournamentSummary> list() {
        return tournaments.findAllByOrderByNameAscIdAsc().stream()
                .map(TournamentSummary::from)
                .toList();
    }

    public TournamentSummary get(UUID id) {
        return tournaments.findById(id)
                .map(TournamentSummary::from)
                .orElseThrow(() -> notFound(id));
    }

    public void requireExists(UUID id) {
        if (!tournaments.existsById(id)) {
            throw notFound(id);
        }
    }

    @Transactional
    public TournamentSummary create(String name) {
        return TournamentSummary.from(tournaments.save(new TournamentEntity(name.strip())));
    }

    private ResourceNotFoundException notFound(UUID id) {
        return new ResourceNotFoundException("Tournament " + id + " was not found.");
    }

    public record TournamentSummary(UUID id, String name) {
        private static TournamentSummary from(TournamentEntity entity) {
            return new TournamentSummary(entity.getId(), entity.getName());
        }
    }
}
