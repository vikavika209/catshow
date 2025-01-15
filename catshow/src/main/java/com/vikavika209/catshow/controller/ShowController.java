package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.ShowRepository;
import com.vikavika209.catshow.service.CatService;
import com.vikavika209.catshow.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShowController {

    private final ShowService showService;
    private final CatService catService;

    @Autowired
    public ShowController(ShowRepository showRepository, ShowService showService, CatService catService) {
        this.showService = showService;
        this.catService = catService;
    }

    public ShowController(ShowService showService, CatService catService) {
        this.showService = showService;
        this.catService = catService;
    }

    @GetMapping("/currentshow")
    public String getCurrentShows(Model model) {
        List<Show> shows = showService.getAllShow();
        model.addAttribute("shows", shows);
        return "currentshow";
    }

    @PostMapping("/add_pet_to_show")
    public String addPetToShow(@RequestParam ("catId") Long catId, @RequestParam ("showId") Long showId, Model model) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        showService.addParticipant(catId, showId);
        List<Show> shows = showService.getAllShow();
        model.addAttribute("shows", shows);
        return "currentshow";
    }
}
