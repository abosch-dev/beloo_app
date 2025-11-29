package com.beloo.test.controller;

import com.beloo.test.model.dto.LoginResponse;
import com.beloo.test.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> getUserToken() {
        return ResponseEntity.ok(new LoginResponse(authService.authenticate("userTest", "somePassword")));
    }
}
