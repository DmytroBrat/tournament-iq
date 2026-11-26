package com.artisyn.tournamentiq.field.api;

import com.artisyn.tournamentiq.field.application.FieldService;
import com.artisyn.tournamentiq.field.application.FieldService.FieldSummary;
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
@RequestMapping("/api/tournaments/{tournamentId}/fields")
class FieldController {
    private final FieldService fields;

    FieldController(FieldService fields) {
        this.fields = fields;
    }

    @GetMapping
    List<FieldSummary> list(@PathVariable UUID tournamentId) {
        return fields.list(tournamentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FieldSummary register(@PathVariable UUID tournamentId, @Valid @RequestBody NameRequest request) {
        return fields.register(tournamentId, request.name());
    }
}
