package com.artisyn.tournamentiq.match.domain;

public class InvalidMatchException extends RuntimeException {
    private final String field;

    public InvalidMatchException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
