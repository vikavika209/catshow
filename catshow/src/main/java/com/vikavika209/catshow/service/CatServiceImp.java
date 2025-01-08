package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
public class CatServiceImp implements CatService{

    CatRepository catRepository;
    OwnerService ownerService;
    ShowService showService;
    FromOptional fromOptional;

    Logger logger = LoggerFactory.getLogger(CatServiceImp.class);

    private void logError(String action, long id, Exception e) {
        logger.error("Failed to {} cat with id {}: {}", action, id, e.getMessage(), e);
    }

    @Autowired
    public CatServiceImp(CatRepository catRepository, OwnerService ownerService, ShowService showService, FromOptional fromOptional) {
        this.catRepository = catRepository;
        this.ownerService = ownerService;
        this.showService = showService;
        this.fromOptional = fromOptional;
    }

    @Transactional
    @Override
    public Cat createCat(String name, Breed breed, long ownerId) throws OwnerNotFoundException {
        Owner owner = ownerService.getOwner(ownerId);
        Cat cat = new Cat(name, breed, owner);
        catRepository.save(cat);
        showService.addPotentialCatsToShowsByCity(cat);
        logger.info("New cat with name {} created", name);
        return cat;
    }

    @Transactional
    @Override
    public Cat getCatById(long id) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException {
        try {
            logger.info("Cat with id {} has been gotten", id);
            return fromOptional.objectFromOptional(Cat.class, id);
        }catch (Exception e) {
            logError("getCatById", id, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }

    @Transactional
    @Override
    public Cat updateCat(long id, String name, Breed breed, long ownerId) throws CatNotFoundException, ShowNotFoundException, OwnerNotFoundException {
        try {
            Cat cat = fromOptional.objectFromOptional(Cat.class, id);
            cat.setName(name);
            cat.setBreed(breed);
            cat.setOwner(ownerService.getOwner(ownerId));
            catRepository.save(cat);
            logger.info("Cat with id {} has been updated", id);
            return cat;
        }catch (Exception e){
            logError("updateCat", id, e);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteCat(long id) throws CatNotFoundException {
        if (!catRepository.existsById(id)) {
            logger.warn("Cat with id {} not found for deletion", id);
            throw new CatNotFoundException("Cat with id " + id + " not found");
        }
        catRepository.deleteById(id);
        logger.info("Cat with id {} has been deleted", id);
    }

    @Override
    public void deleteAll() {
        catRepository.deleteAll();
    }

    @Transactional
    @Override
    public Set<Show> getShowByCatId(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Cat cat = fromOptional.objectFromOptional(Cat.class, id);
        return cat.getParticipatedShows();
    }
}
