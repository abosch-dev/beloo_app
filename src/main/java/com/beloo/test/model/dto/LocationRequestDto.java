package com.beloo.test.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationRequestDto(
        @NotNull(message = "cyclistId no puede ser nulo")
        @NotBlank(message = "cyclistId no puede estar en blanco")
        String cyclistId,

        @NotNull(message = "latitude no puede ser nulo")
        Double latitude,

        @NotNull(message = "longitude no puede ser nulo")
        Double longitude
) {
}
