package com.beloo.test.controller;

import com.beloo.test.model.dto.LocationRequestDto;
import com.beloo.test.service.LocationService;
import com.beloo.test.service.RedisGeoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private RedisGeoService redisGeoService;
    @Mock
    private LocationService locationService;
    @InjectMocks
    private LocationController locationController;

    @Test
    void addLocation_should_call_add_location_for_save_location_history() {
        var locationRequestDto = new LocationRequestDto("1", 40.7128, -74.0060);

        locationController.addLocation(locationRequestDto);

        var requestCaptor = ArgumentCaptor.forClass(LocationRequestDto.class);
        verify(locationService, times(1)).saveLocation(requestCaptor.capture());
        verifyNoMoreInteractions(locationService);
        assertEquals(locationRequestDto, requestCaptor.getValue());
    }

    @Test
    void addLocation_should_call_redis_for_update_last_location_in_redis() {
        var locationRequestDto = new LocationRequestDto("1", 40.7128, -74.0060);

        locationController.addLocation(locationRequestDto);

        var requestCaptor = ArgumentCaptor.forClass(LocationRequestDto.class);
        verify(redisGeoService, times(1)).addCyclistLocation(requestCaptor.capture());
        verifyNoMoreInteractions(redisGeoService);
        assertEquals(locationRequestDto, requestCaptor.getValue());
    }

    @Test
    void find_nearby_locations_should_get_location_from_redis() {
        double latitude = 40.7128;
        double longitude = -74.0060;

        locationController.getLocations(latitude, longitude);

        var latitudeCaptor = ArgumentCaptor.forClass(Double.class);
        var longitudeCaptor = ArgumentCaptor.forClass(Double.class);
        verify(redisGeoService, times(1)).findNearbyCyclists(latitudeCaptor.capture(), longitudeCaptor.capture(), any(Double.class), any(Metrics.class));
        verifyNoMoreInteractions(redisGeoService);
        assertEquals(latitude, latitudeCaptor.getValue());
        assertEquals(longitude, longitudeCaptor.getValue());
    }
}