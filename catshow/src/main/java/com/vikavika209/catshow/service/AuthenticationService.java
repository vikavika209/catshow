package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final OwnerService ownerService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public String signUp(String name, String email, String password, String city) {
        Owner owner = ownerService.createOwner(name, email, password, city);
        return jwtUtil.generateToken(owner);
    }

    public String signIn(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                email, password
                ));

        var owner = (Owner) authentication.getPrincipal();

        return jwtUtil.generateToken(owner);
    }
}
