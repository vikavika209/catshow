package com.vikavika209.catshow.service;

import com.vikavika209.catshow.controller.OwnerController;
import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.EmailAlreadyExistsException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class OwnerServiceImp implements OwnerService{

    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final ShowService showService;
    private final FromOptional fromOptional;
    private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImp.class);

    @Autowired
    public OwnerServiceImp(PasswordEncoder passwordEncoder, OwnerRepository ownerRepository, ShowService showService, FromOptional fromOptional) {
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
        this.showService = showService;
        this.fromOptional = fromOptional;
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

    @Transactional
    @Override
    public Owner verifyOwner(String email, String password) throws OwnerNotFoundException {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new OwnerNotFoundException("Пользователь с email: " + email + " не найден"));

        logger.info("The owner has been found with email: {}", email);
        logger.info("The password for the owner is '{}'", owner.getPassword());

        if (!passwordEncoder.matches(password, owner.getPassword())) {
            throw new OwnerNotFoundException("Неверный пароль для email: " + email);
        }
        return owner;
    }

    @Override
    public void deleteAll() {
        ownerRepository.deleteAll();
    }
}
