package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface CatService {
    Cat createCat(String name, Breed breed, long ownerId) throws OwnerNotFoundException;
    Cat getCatById(long id) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException;
    List<Cat> getAllCats();
    Cat updateCat(long id, String name, Breed breed, long ownerId) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException;
    void deleteCat(long id) throws CatNotFoundException;
    void deleteAll();
    Set<Show> getShowByCatId(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    List<Cat> allCats();
}
