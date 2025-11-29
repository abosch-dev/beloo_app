package com.beloo.test.controller;


import com.beloo.test.model.dto.CyclistResponseDto;
import com.beloo.test.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/cyclist")
@Tag(name = "Cyclist", description = "Gestión de las rutas de un ciclista")
public class CyclistController {

    @Autowired
    private LocationService locationService;

    @GetMapping(path = "/{idCyclist}/route", produces = {APPLICATION_JSON_VALUE})
    @Operation(
            summary = "Obtener las rutas de un ciclista determinado",
            description = "Se obtienen los puntos asociados a un día, describiendo así una ruta del día"
    )
    public ResponseEntity<CyclistResponseDto> getLocations(
            @PathVariable(name = "idCyclist")
            @Parameter(description = "Id del ciclista del cual se desea obtener la ruta")
            String idCyclist
    ) {
        return ResponseEntity.ok(locationService.getCyclistLocations(idCyclist));
    }
}
