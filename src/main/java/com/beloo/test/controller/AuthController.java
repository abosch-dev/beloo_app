package com.beloo.test.controller;

import com.beloo.test.model.dto.LoginResponse;
import com.beloo.test.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Autenticación de los usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    @Operation(
            summary = "Obtener el token",
            description = "Obtener el token a usar en el resto de la API"
    )
    public ResponseEntity<LoginResponse> getUserToken() {
        return ResponseEntity.ok(new LoginResponse(authService.authenticate("userTest", "somePassword")));
    }
}
