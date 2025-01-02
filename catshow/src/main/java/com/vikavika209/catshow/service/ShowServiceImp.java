package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShowServiceImp implements ShowService{

    private ShowRepository showRepository;
    private CatRepository catRepository;

    @Autowired
    public ShowServiceImp(ShowRepository showRepository, CatRepository catRepository) {
        this.showRepository = showRepository;
        this.catRepository = catRepository;
    }

    @Override
    public Show createShow(String city, Date date, String address) {
        return null;
    }

    @Override
    public Show updateShow(long id, String city, Date date, String address) {
        return null;
    }

    @Override
    public List<Show> getAllShow() {
        return List.of();
    }

    @Override
    public void deleteShow(long id) {

    }

    @Override
    @Transactional
    public void addCatsToShowsByCity(Cat cat) {
        List<Show> shows = showRepository.findAll();
        for (Show show : shows) {
            if(cat.getOwner().getCity().equals(show.getCity())){
                show.getCats().add(cat);
            }
        }
    }
}