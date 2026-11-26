CREATE TABLE tournaments (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL CHECK (length(trim(name)) > 0)
);

CREATE TABLE teams (
    id UUID PRIMARY KEY,
    tournament_id UUID NOT NULL REFERENCES tournaments (id),
    name VARCHAR(120) NOT NULL CHECK (length(trim(name)) > 0),
    UNIQUE (tournament_id, id)
);

CREATE TABLE fields (
    id UUID PRIMARY KEY,
    tournament_id UUID NOT NULL REFERENCES tournaments (id),
    name VARCHAR(120) NOT NULL CHECK (length(trim(name)) > 0),
    UNIQUE (tournament_id, id)
);

CREATE TABLE matches (
    id UUID PRIMARY KEY,
    tournament_id UUID NOT NULL REFERENCES tournaments (id),
    home_team_id UUID NOT NULL,
    away_team_id UUID NOT NULL,
    field_id UUID NOT NULL,
    starts_at TIMESTAMPTZ NOT NULL,
    ends_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (tournament_id, home_team_id) REFERENCES teams (tournament_id, id),
    FOREIGN KEY (tournament_id, away_team_id) REFERENCES teams (tournament_id, id),
    FOREIGN KEY (tournament_id, field_id) REFERENCES fields (tournament_id, id),
    CHECK (home_team_id <> away_team_id),
    CHECK (ends_at > starts_at)
);

CREATE INDEX matches_schedule_idx ON matches (tournament_id, starts_at, id);
