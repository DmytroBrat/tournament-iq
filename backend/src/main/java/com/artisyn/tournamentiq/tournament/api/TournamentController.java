package com.artisyn.tournamentiq.tournament.api;

import com.artisyn.tournamentiq.shared.api.NameRequest;
import com.artisyn.tournamentiq.tournament.application.TournamentService;
import com.artisyn.tournamentiq.tournament.application.TournamentService.TournamentSummary;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
class TournamentController {
    private final TournamentService tournaments;

    TournamentController(TournamentService tournaments) {
        this.tournaments = tournaments;
    }

    @GetMapping
    List<TournamentSummary> list() {
        return tournaments.list();
    }

    @GetMapping("/{id}")
    TournamentSummary get(@PathVariable UUID id) {
        return tournaments.get(id);
    }

    @PostMapping
    ResponseEntity<TournamentSummary> create(@Valid @RequestBody NameRequest request) {
        TournamentSummary tournament = tournaments.create(request.name());
        return ResponseEntity.created(URI.create("/api/tournaments/" + tournament.id())).body(tournament);
    }
}
