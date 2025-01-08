package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public interface ShowService {
    Show createShow(String city, Date date, String address);
    Show updateShow(long id, String city, Date date, String address) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    Show getShowById(Long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    Set<Cat> getPotentialParticipantsByCity(String city);
    List<Show> getAllShow();
    void addPotentialCatsToShowsByCity(Cat cat);
    void addParticipant(long catId, long showId) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    void deleteShow(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    void deleteAll();
}
