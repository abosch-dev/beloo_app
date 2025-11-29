package com.beloo.test.model.dto;

import java.util.List;

public record CyclistResponseDto(
        List<CyclistDateCoordinates> data
) {
}
