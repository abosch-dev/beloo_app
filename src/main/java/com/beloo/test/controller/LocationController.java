package com.beloo.test.controller;

import com.beloo.test.model.dto.LocationRequestDto;
import com.beloo.test.model.dto.LocationResponseDto;
import com.beloo.test.service.LocationService;
import com.beloo.test.service.RedisGeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Metrics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/locations")
@Tag(name = "Locations", description = "Gestión de localizaciones de un ciclista")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private RedisGeoService redisGeoService;
    @Value("${beloo.location.meters}")
    private int meters;

    @PostMapping(consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    @Operation(
            summary = "Actualizar la localización del ciclista",
            description = "Actualiza la localización del ciclista y la almacena en el histórico para poder construir la ruta"
    )
    public ResponseEntity<String> addLocation(@Valid @RequestBody LocationRequestDto location) {
        redisGeoService.addCyclistLocation(location);
        locationService.saveLocation(location);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/nearby", produces = {APPLICATION_JSON_VALUE})
    @Operation(
            summary = "Obtener los ciclistas cercanos",
            description = "Obtiene los ciclistas a 500 metros con respecto a unas coordenadas dadas"
    )
    public ResponseEntity<LocationResponseDto> getLocations(@RequestParam Double latitude, @RequestParam Double longitude) {
        var results = redisGeoService.findNearbyCyclists(latitude, longitude, meters, Metrics.KILOMETERS);
        return ResponseEntity.ok(new LocationResponseDto(results));
    }

}
