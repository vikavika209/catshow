package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.OwnerRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FromOptional {

    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private final ShowRepository showRepository;

    @Autowired
    public FromOptional(CatRepository catRepository, OwnerRepository ownerRepository, ShowRepository showRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.showRepository = showRepository;
    }

    @SuppressWarnings("unchecked")
    public <T> T objectFromOptional(Class<T> type, long id) {
        if (type == Cat.class) {
            return (T) catRepository.findById(id)
                    .orElseThrow(() -> new CatNotFoundException("Cat not found with id: " + id));
        }
        if (type == Owner.class) {
            return (T) ownerRepository.findById(id)
                    .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + id));
        }
        if (type == Show.class) {
            return (T) showRepository.findById(id)
                    .orElseThrow(() -> new ShowNotFoundException("Show not found with id: " + id));
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
    }
}
