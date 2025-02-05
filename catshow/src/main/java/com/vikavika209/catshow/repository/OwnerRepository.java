package com.vikavika209.catshow.repository;

import com.vikavika209.catshow.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByUsername(String email);

    Optional<Owner> findByUsername(String email);
}