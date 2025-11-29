package com.beloo.test.model.dto;

import java.time.LocalDate;
import java.util.List;

public record CyclistDateCoordinates(
        LocalDate date,
        List<Coordinate> locations
) {
}