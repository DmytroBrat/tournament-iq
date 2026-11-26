package com.artisyn.tournamentiq.match.persistence;

import com.artisyn.tournamentiq.field.persistence.FieldEntity;
import com.artisyn.tournamentiq.team.persistence.TeamEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tournamentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id")
    private TeamEntity homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id")
    private TeamEntity awayTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id")
    private FieldEntity field;

    private Instant startsAt;
    private Instant endsAt;

    protected MatchEntity() {}

    public MatchEntity(UUID tournamentId, TeamEntity homeTeam, TeamEntity awayTeam,
                       FieldEntity field, Instant startsAt, Instant endsAt) {
        this.tournamentId = tournamentId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.field = field;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public UUID getId() {
        return id;
    }

    public TeamEntity getHomeTeam() {
        return homeTeam;
    }

    public TeamEntity getAwayTeam() {
        return awayTeam;
    }

    public FieldEntity getField() {
        return field;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }
}
