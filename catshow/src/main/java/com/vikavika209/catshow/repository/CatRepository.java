package com.vikavika209.catshow.repository;

import com.vikavika209.catshow.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByOwner_City(String city);
}
