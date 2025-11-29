package com.beloo.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.singleton;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        var authority = new SimpleGrantedAuthority("ADMIN");

        //Lo haré con un user fake para evitarme la carga de usuarios pero normalmente haríamos consulta a base de datos para saber si el usuario existe
        return new User(userName, passwordEncoder.encode("somePassword"), singleton(authority));
    }
}
