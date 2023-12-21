package com.example.amedigitalswapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeletePlanetDTO(@NotNull UUID id) {
}
