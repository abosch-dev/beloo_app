package com.beloo.test.service;

import com.beloo.test.model.dto.LocationRequestDto;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisGeoService {

    private static final String LOCATION_GEOSPATIAL_KEY = "cyclistLocations";

    private final GeoOperations<String, String> geoOperations;

    public RedisGeoService(StringRedisTemplate template) {
        this.geoOperations = template.opsForGeo();
    }

    public void addCyclistLocation(LocationRequestDto location) {
        Point point = new Point(location.longitude(), location.latitude());
        geoOperations.add(LOCATION_GEOSPATIAL_KEY, point, location.cyclistId());
    }

    public List<String> findNearbyCyclists(
            double lat, double lon, double radius, Metrics metric
    ) {
        Point center = new Point(lon, lat);
        var distance = new Distance(radius / 1000, metric);
        var searchCircle = new Circle(center, distance);
        return geoOperations.radius(LOCATION_GEOSPATIAL_KEY, searchCircle).getContent().stream()
                .map(geoResult -> geoResult.getContent().getName())
                .toList();
    }
}
