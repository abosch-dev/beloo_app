package com.beloo.test.controller;


import com.beloo.test.model.dto.CyclistResponseDto;
import com.beloo.test.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/cyclist")
public class CyclistController {

    @Autowired
    private LocationService locationService;

    @GetMapping(path = "/{idCyclist}/route", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CyclistResponseDto> getLocations(@PathVariable(name = "idCyclist") String idCyclist) {
        return ResponseEntity.ok(locationService.getCyclistLocations(idCyclist));
    }
}
