package com.artisyn.tournamentiq.team.api;

import com.artisyn.tournamentiq.team.application.TeamService;
import com.artisyn.tournamentiq.team.application.TeamService.TeamSummary;
import com.artisyn.tournamentiq.shared.api.NameRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/teams")
class TeamController {
    private final TeamService teams;

    TeamController(TeamService teams) {
        this.teams = teams;
    }

    @GetMapping
    List<TeamSummary> list(@PathVariable UUID tournamentId) {
        return teams.list(tournamentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TeamSummary register(@PathVariable UUID tournamentId, @Valid @RequestBody NameRequest request) {
        return teams.register(tournamentId, request.name());
    }
}
