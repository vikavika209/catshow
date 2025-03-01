package com.vikavika209.catshow.repository;

import com.vikavika209.catshow.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByOwner_City(String city);

    @Query("SELECT c FROM Cat c WHERE c.owner.city = :city")
    List<Cat> findCatsByCity(@Param("city") String city);
}
