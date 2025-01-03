package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;

public interface CatService {
    Cat createCat(String name, Breed breed, long ownerId) throws OwnerNotFoundException;
    Cat getCatById(long id) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException;
    Cat updateCat(long id, String name, Breed breed, long ownerId) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException;
    void deleteCat(long id) throws CatNotFoundException;
}
