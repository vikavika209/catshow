package com.vikavika209.catshow.service;

import com.vikavika209.catshow.controller.OwnerController;
import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.EmailAlreadyExistsException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Role;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.OwnerRepository;
import com.vikavika209.catshow.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class OwnerServiceImp implements OwnerService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final ShowService showService;
    private final FromOptional fromOptional;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImp.class);

    @Autowired
    public OwnerServiceImp(@Lazy PasswordEncoder passwordEncoder, OwnerRepository ownerRepository, ShowService showService, FromOptional fromOptional, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
        this.showService = showService;
        this.fromOptional = fromOptional;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public Owner createOwner(String name, String email, String rawPassword, String city) {
        if (ownerRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Пользователь с email: " + email + " уже существует");
        }

        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        Owner owner = new Owner(email, encodedPassword, name, city);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        owner.setRoles(roles);
        ownerRepository.save(owner);
        return owner;
    }

    @Transactional
    @Override
    public Owner getOwner(long id) throws OwnerNotFoundException {
        return ownerFromOptional(id);
    }

    @Transactional
    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @Transactional
    @Override
    public Set<Cat> getCatsOfTheOwnerById(long ownerId) throws OwnerNotFoundException {
        Owner owner = ownerFromOptional(ownerId);
        List<Cat> cats = owner.getCats();
        return Set.copyOf(cats);
    }

    @Transactional
    @Override
    public Set<Show> getAllShowsByOwnerId(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Owner owner = fromOptional.objectFromOptional(Owner.class, id);
        var cats = owner.getCats();
        return cats.stream()
                .flatMap(cat -> cat.getParticipatedShows().stream())
                .collect(Collectors.toSet());
    }


    @Transactional
    @Override
    public Owner updateOwner(long id, String name, String email, String password, String city) throws OwnerNotFoundException {
        Owner owner = ownerFromOptional(id);
        owner.setName(name);
        owner.setEmail(email);
        owner.setPassword(passwordEncoder.encode(password));
        owner.setCity(city);
        return owner;
    }

    @Transactional
    @Override
    public void deleteOwner(long id) {
        ownerRepository.deleteById(id);
    }

    private Owner ownerFromOptional(long id) throws OwnerNotFoundException {
        Optional<Owner> optionalOwner = ownerRepository.findById(id);
        return optionalOwner.orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + id));
    }

//    @Transactional
//    @Override
//    public Owner verifyOwner(String email, String password) throws OwnerNotFoundException {
//        Owner verifyOwner = (Owner) loadUserByUsername(email);
//
//        if (verifyOwner != null) {
//            logger.info("Пользователь с email: {} успешно найден", email);
//
//            if(verifyOwner.getPassword().equals(passwordEncoder.encode(password))) {
//                String token = jwtUtil.generateToken(email);
//                return verifyOwner;
//            }else {
//                logger.error("Неверный пароль для логина: {}", email);
//                throw new OwnerNotFoundException("Неверный пароль для логина: " + email);
//            }
//        }else {
//            logger.debug("Пользователь с логином {} не найден", email);
//            return null;
//        }
//    }

    @Override
    public void deleteAll() {
        ownerRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            logger.error("Логин передан пустым! Возможно, проблема в передаче параметров формы.");
            throw new UsernameNotFoundException("Логин не может быть пустым");
        }

        logger.info("Ищем пользователя с email: {}", username);

        return ownerRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.warn("Пользователь с email {} не найден", username);
                    return new UsernameNotFoundException("Пользователь с email " + username + " не найден");
                });
    }

    @Override
    public Owner getCurrentOwner() throws OwnerNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new OwnerNotFoundException("Ошибка при получении текущего пользователя");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Owner) {
            return (Owner) principal;
        } else {
            throw new OwnerNotFoundException("Текущий пользователь не является владельцем");
        }
    }
}
