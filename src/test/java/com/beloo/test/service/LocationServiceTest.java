package com.beloo.test.service;

import com.beloo.test.entity.Location;
import com.beloo.test.model.dto.LocationRequestDto;
import com.beloo.test.repository.LocationRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.beloo.test.service.LocationService.GEOMETRY_FACTORY;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private LocationService locationService;

    @Test
    void save_location_should_call_repository_save_location() {
        var userId = "1";
        double lat = 40.7128;
        double lon = -74.0060;

        locationService.saveLocation(new LocationRequestDto(userId, lat, lon));

        verify(locationRepository, times(1)).save(any(Location.class));
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    void get_cyclist_locations_should_get_ordered_locations_according_to_instant_save() {
        var userId = "1";
        var coordinate1 = new Coordinate(-74.0060, 40.7128);
        var coordinate2 = new Coordinate(-74.0061, 40.7129);
        var location1 = new Location(1, userId, Instant.now().plusSeconds(100), GEOMETRY_FACTORY.createPoint(coordinate1));
        var location2 = new Location(1, userId, Instant.now(), GEOMETRY_FACTORY.createPoint(coordinate2));
        when(locationRepository.findLocationsByCyclist(userId)).thenReturn(List.of(location1, location2));

        var results = locationService.getCyclistLocations(userId);

        verify(locationRepository, times(1)).findLocationsByCyclist(userId);
        verifyNoMoreInteractions(locationRepository);
        assertThat(results.data(), Matchers.hasSize(1));
        assertAll(() -> {
            assertThat(results.data().get(0).date(), Matchers.is(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault())));
            assertThat(results.data().get(0).locations(), Matchers.hasSize(2));
            assertThat(results.data().get(0).locations().stream().map(com.beloo.test.model.dto.Coordinate::lat).toList(), Matchers.containsInRelativeOrder(coordinate2.y, coordinate1.y));
        });
    }

    @Test
    void get_empty_cyclist_locations_should_get_empty_locations() {
        var userId = "1";
        when(locationRepository.findLocationsByCyclist(userId)).thenReturn(emptyList());

        var results = locationService.getCyclistLocations(userId);

        verify(locationRepository, times(1)).findLocationsByCyclist(userId);
        verifyNoMoreInteractions(locationRepository);
        assertTrue(results.data().isEmpty());
    }
}