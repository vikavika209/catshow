package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShowServiceImp implements ShowService{

    private final FromOptional fromOptional;
    private ShowRepository showRepository;
    private CatRepository catRepository;

    @Autowired
    public ShowServiceImp(ShowRepository showRepository, CatRepository catRepository, FromOptional fromOptional) {
        this.showRepository = showRepository;
        this.catRepository = catRepository;
        this.fromOptional = fromOptional;
    }

    @Transactional
    @Override
    public Show createShow(String city, Date date, String address) {
        Show show = new Show(city, date, address);
        List<Cat> cats = catRepository.findCatsByCity(city);
        Set<Cat> potentialParticipants = new HashSet<>(cats);
        show.setPotentialParticipants(potentialParticipants);
        showRepository.save(show);
        return show;
    }

    @Transactional
    @Override
    public Show updateShow(long id, String city, Date date, String address) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Show show = fromOptional.objectFromOptional(Show.class, id);
        show.setCity(city);
        show.setDate(date);
        show.setAddress(address);
        return show;
    }

    @Transactional
    @Override
    public List<Show> getAllShow() {
        return showRepository.findAll();
    }

    @Transactional
    @Override
    public Set<Cat> getPotentialParticipantsByCity(String city) {
        List<Cat> cats = catRepository.findAll();
        return cats.stream()
                .filter(cat -> cat.getOwner().getCity().equals(city))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void deleteShow(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Show show = fromOptional.objectFromOptional(Show.class, id);
        showRepository.delete(show);
    }

    @Override
    @Transactional
    public void addPotentialCatsToShowsByCity(Cat cat) {
        List<Show> shows = showRepository.findAll();
        for (Show show : shows) {
            if(cat.getOwner().getCity().equals(show.getCity())){
                show.getPotentialParticipants().add(cat);
            }
        }
    }

    @Transactional
    @Override
    public void addParticipant(long catId, long showId) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Cat cat = fromOptional.objectFromOptional(Cat.class, catId);
        Show show = fromOptional.objectFromOptional(Show.class, showId);
        show.getParticipants().add(cat);

        Set<Cat> potentialParticipants = show.getPotentialParticipants();
        potentialParticipants.remove(cat);
    }

    @Transactional
    @Override
    public void deleteAll() {
        showRepository.deleteAll();
    }

    @Transactional
    @Override
    public Show getShowById(Long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        return fromOptional.objectFromOptional(Show.class, id);
    }
}