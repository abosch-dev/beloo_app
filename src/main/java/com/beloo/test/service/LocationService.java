package com.beloo.test.service;

import com.beloo.test.entity.Location;
import com.beloo.test.model.dto.CyclistDateCoordinates;
import com.beloo.test.model.dto.CyclistResponseDto;
import com.beloo.test.model.dto.LocationRequestDto;
import com.beloo.test.repository.LocationRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationService {

    protected static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired
    private LocationRepository locationRepository;

    public void saveLocation(LocationRequestDto locationRequest) {
        locationRepository.save(mapLocation(locationRequest));
    }

    private Location mapLocation(LocationRequestDto locationRequest) {
        var location = new Location();
        location.setCiclystId(locationRequest.cyclistId());
        location.setTimeStamp(Instant.now());
        var coordinate = new Coordinate(locationRequest.longitude(), locationRequest.latitude());
        location.setPosition(GEOMETRY_FACTORY.createPoint(coordinate));
        return location;
    }

    public CyclistResponseDto getCyclistLocations(String cyclistId) {
        var locations = groupLocationsByDate(locationRepository.findLocationsByCyclist(cyclistId));
        return new CyclistResponseDto(locations.entrySet().stream().map(it -> mapCyclistDateCoordinates(it.getValue(), it.getKey())).toList());
    }

    private static Map<LocalDate, List<Location>> groupLocationsByDate(List<Location> locations) {
        return locations.stream().collect(Collectors.groupingBy(
                it -> LocalDate.ofInstant(it.getTimeStamp(), ZoneId.systemDefault()),
                Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(Location::getTimeStamp)).toList())
        ));
    }

    private static CyclistDateCoordinates mapCyclistDateCoordinates(List<Location> locations, LocalDate date) {
        return new CyclistDateCoordinates(date, locations.stream().map(it -> new com.beloo.test.model.dto.Coordinate(it.getPosition().getX(), it.getPosition().getY())).collect(Collectors.toList()));
    }
}
