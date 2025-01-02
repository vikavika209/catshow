package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @GetMapping("/currentshow")
    public String getCurrentShows(Model model) {
        List<Show> shows = showRepository.findAll();
        model.addAttribute("shows", shows);
        return "currentshow";
    }
}
