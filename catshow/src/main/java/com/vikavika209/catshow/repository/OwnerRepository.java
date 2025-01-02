package com.vikavika209.catshow.repository;

import com.vikavika209.catshow.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}