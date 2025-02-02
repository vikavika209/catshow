package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.service.OwnerService;
import com.vikavika209.catshow.utils.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class OwnerController {
    private final OwnerService ownerService;
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(OwnerService ownerService, JwtUtil jwtUtil) {
        this.ownerService = ownerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("owner", new Owner());
        return "registration";
    }

    @PostMapping("/submit_registration")
    public String registerOwner(
            @Valid @ModelAttribute Owner owner,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "registration";
        }

        try {
            ownerService.createOwner(owner.getName(), owner.getEmail(), owner.getPassword(), owner.getCity());
            logger.info("Пользователь с email: {} успешно создан", owner.getEmail());
            return "registration-success";
        } catch (Exception e) {
            logger.error("Ошибка при регистрации: ", e);
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/my_show/{id}")
    public String getOwnerShows(@PathVariable("id") long ownerId, Model model) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Set<Show> shows = ownerService.getAllShowsByOwnerId(ownerId);
        model.addAttribute("shows", shows);
        model.addAttribute("ownerId", ownerId);
        return "my_show";
    }

    @GetMapping("/profile")
    public String profile(Model model) throws OwnerNotFoundException, ShowNotFoundException, CatNotFoundException {
        Owner owner = ownerService.getCurrentOwner();

        String catNames = owner.getCats() != null
                ? ownerService.getCatsOfTheOwnerById(owner.getId()).stream().map(Cat::getName).collect(Collectors.joining(", "))
                : "No cat has been found";

        model.addAttribute("owner", owner);
        model.addAttribute("catNames", catNames);

        return "my_profile";
    }

    @GetMapping("/my_pet/{id}")
    public String getCatsByOwnerId(@PathVariable ("id") long ownerId, Model model) throws CatNotFoundException, OwnerNotFoundException {

        Owner owner = ownerService.getOwner(ownerId);
        Set<Cat> cats = ownerService.getCatsOfTheOwnerById(ownerId);

        model.addAttribute("cats", cats);
        model.addAttribute("ownerId", ownerId);

        return "my_pets";
    }

    @GetMapping("/owner/home/{id}")
    public String getHomePage(@PathVariable("id") long ownerId, Model model) throws OwnerNotFoundException {

        Owner owner = ownerService.getOwner(ownerId);

        model.addAttribute("ownerId", ownerId);

        return "home_page";
    }

}