package com.beloo.test.service;

import com.beloo.test.model.dto.LocationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisGeoServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private GeoOperations<String, String> geoOperations;
    @InjectMocks
    private RedisGeoService redisGeoService;

    @BeforeEach
    void setUp() {
        when(stringRedisTemplate.opsForGeo()).thenReturn(geoOperations);
        redisGeoService = new RedisGeoService(stringRedisTemplate);
    }

    @Test
    void given_location_request_should_add_location_into_geoperations() {
        String userId = "user123";
        double lat = 40.7128;
        double lon = -74.0060;
        var locationDto = new LocationRequestDto(userId, lat, lon);

        redisGeoService.addCyclistLocation(locationDto);

        verify(geoOperations, times(1)).add(
                eq("cyclistLocations"),
                argThat(point -> point.getX() == lon && point.getY() == lat),
                eq(userId)
        );
        verifyNoMoreInteractions(geoOperations);
    }

    @Test
    void given_coordinates_should_return_cyclist_nearby() {
        double lat = 40.7128;
        double lon = -74.0060;
        int radius = 500;
        GeoResults<RedisGeoCommands.GeoLocation<String>> mockResults = new GeoResults<>(emptyList());
        when(geoOperations.radius(anyString(), any(Circle.class))).thenReturn(mockResults);

        var result = redisGeoService.findNearbyCyclists(lat, lon, radius, Metrics.KILOMETERS);

        assertNotNull(result, "El resultado no debe ser nulo");
        verify(geoOperations, times(1)).radius(
                eq("cyclistLocations"),
                argThat(circle -> {
                    boolean centerMatches = circle.getCenter().getX() == lon && circle.getCenter().getY() == lat;
                    boolean distanceMatches = circle.getRadius().getValue() == 0.5 &&
                            circle.getRadius().getMetric() == Metrics.KILOMETERS;
                    return centerMatches && distanceMatches;
                })
        );
    }

}