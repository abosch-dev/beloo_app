package com.beloo.test.service;

import com.beloo.test.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AuthService(JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public String authenticate(String username, String password){
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        var authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        return jwtUtil.generateToken(authResult);
    }
}
