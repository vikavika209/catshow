package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerServiceImp implements OwnerService{

    private final PasswordEncoder passwordEncoder;
    private OwnerRepository ownerRepository;

    @Autowired
    public OwnerServiceImp(PasswordEncoder passwordEncoder, OwnerRepository ownerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner createOwner(String name, String email, String rawPassword, String city) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Owner owner = new Owner(email, encodedPassword, name, city);
        ownerRepository.save(owner);
        return owner;
    }

    @Override
    public Owner getOwner(long id) {
        return ownerFromOptional(id);
    }

    @Override
    public Owner updateOwner(long id, String name, String email, String password, String city) {
        Owner owner = ownerFromOptional(id);
        owner.setName(name);
        owner.setEmail(email);
        owner.setPassword(passwordEncoder.encode(password));
        owner.setCity(city);
        return owner;
    }

    @Override
    public void deleteOwner(long id) {
        ownerRepository.deleteById(id);
    }

    private Owner ownerFromOptional(long id){
        Optional<Owner> optionalOwner = ownerRepository.findById(id);
        return optionalOwner.orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + id));
    }
}
