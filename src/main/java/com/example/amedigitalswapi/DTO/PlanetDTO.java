package com.example.amedigitalswapi.DTO;

import jakarta.validation.constraints.NotBlank;

public record PlanetDTO(@NotBlank String name,
                        @NotBlank String climate,
                        @NotBlank String terrain) {
}
