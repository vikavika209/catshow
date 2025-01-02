package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;

public interface CatService {
    Cat createCat(String name, Breed breed, long ownerId);
    Cat getCatById(long id);
    Cat updateCat(long id, String name, Breed breed, long ownerId);
    void deleteCat(long id);
}
