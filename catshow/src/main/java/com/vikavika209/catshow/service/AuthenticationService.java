package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final OwnerService ownerService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public String signUp(String name, String email, String password, String city) {
        try {
            ownerService.loadUserByUsername(email);
            logger.error("Пользователь с логином {} уже существует", email);
            return signIn(email, password);
        } catch (UsernameNotFoundException e) {
            Owner owner = ownerService.createOwner(name, email, password, city);
            logger.info("Пользователь с email: {} успешно создан", owner.getUsername());
            return jwtUtil.generateToken(owner);
        }
    }

    public String signIn(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            var owner = (Owner) authentication.getPrincipal();
            return jwtUtil.generateToken(owner);
        } catch (BadCredentialsException e) {
            logger.error("Ошибка аутентификации для пользователя: {}", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверные учетные данные");
        }
    }

}
