package com.artisyn.tournamentiq.field.application;

import com.artisyn.tournamentiq.field.persistence.FieldEntity;
import com.artisyn.tournamentiq.field.persistence.FieldRepository;
import com.artisyn.tournamentiq.tournament.application.TournamentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FieldService {
    private final FieldRepository fields;
    private final TournamentService tournaments;

    public FieldService(FieldRepository fields, TournamentService tournaments) {
        this.fields = fields;
        this.tournaments = tournaments;
    }

    public List<FieldSummary> list(UUID tournamentId) {
        tournaments.requireExists(tournamentId);
        return fields.findByTournamentIdOrderByNameAscIdAsc(tournamentId).stream()
                .map(FieldSummary::from)
                .toList();
    }

    @Transactional
    public FieldSummary register(UUID tournamentId, String name) {
        tournaments.requireExists(tournamentId);
        return FieldSummary.from(fields.save(new FieldEntity(tournamentId, name.strip())));
    }

    public record FieldSummary(UUID id, String name) {
        private static FieldSummary from(FieldEntity entity) {
            return new FieldSummary(entity.getId(), entity.getName());
        }
    }
}
