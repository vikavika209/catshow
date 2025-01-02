package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;

import java.util.Date;
import java.util.List;

public interface ShowService {
    Show createShow(String city, Date date, String address);
    Show updateShow(long id, String city, Date date, String address);
    List<Show> getAllShow();
    void addCatsToShowsByCity(Cat cat);
    void deleteShow(long id);
}
