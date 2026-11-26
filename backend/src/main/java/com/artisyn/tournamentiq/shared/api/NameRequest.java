package com.artisyn.tournamentiq.shared.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NameRequest(
        @NotBlank(message = "Enter a name.")
        @Size(max = 120, message = "Use at most 120 characters.")
        String name
) {}
