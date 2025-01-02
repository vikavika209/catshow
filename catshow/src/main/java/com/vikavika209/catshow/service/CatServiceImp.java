package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatServiceImp implements CatService{

    CatRepository catRepository;
    OwnerService ownerService;
    ShowServiceImp showService;
    FromOptional fromOptional;

    @Autowired
    public CatServiceImp(CatRepository catRepository, OwnerService ownerService, ShowServiceImp showService, FromOptional fromOptional) {
        this.catRepository = catRepository;
        this.ownerService = ownerService;
        this.showService = showService;
        this.fromOptional = fromOptional;
    }

    @Override
    public Cat createCat(String name, Breed breed, long ownerId) {
        Owner owner = ownerService.getOwner(ownerId);
        Cat cat = new Cat(name, breed, owner);
        showService.addCatsToShowsByCity(cat);
        return cat;
    }

    @Override
    public Cat getCatById(long id) {
        return fromOptional.objectFromOptional(Cat.class, id);
    }

    @Override
    public Cat updateCat(long id, String name, Breed breed, long ownerId) {
        Cat cat = fromOptional.objectFromOptional(Cat.class, id);
        cat.setName(name);
        cat.setBreed(breed);
        cat.setOwner(ownerService.getOwner(ownerId));
        return cat;
    }

    @Override
    public void deleteCat(long id) {
        catRepository.deleteById(id);
    }
}
