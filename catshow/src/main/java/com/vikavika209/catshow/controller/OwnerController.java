package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.service.CatService;
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
    private final CatService catService;
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(OwnerService ownerService, CatService catService, JwtUtil jwtUtil) {
        this.ownerService = ownerService;
        this.catService = catService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("owner", new Owner());
        return "registration";
    }

    @PostMapping("/submit_registration")
    public String registerOwner(
            @ModelAttribute("owner") @Valid Owner owner,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            logger.info("Ошибка при регистрации пользователя в OwnerController классе");
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

    @GetMapping("/my_show")
    public String getOwnerShows(Model model) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Owner owner = ownerService.getCurrentOwner();
        Set<Show> shows = ownerService.getAllShowsByOwnerId(owner.getId());
        model.addAttribute("shows", shows);
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

    @GetMapping("/my_pets")
    public String getCatsByOwnerId(Model model) throws CatNotFoundException, OwnerNotFoundException {
        Owner owner = ownerService.getCurrentOwner();
        Set<Cat> cats = ownerService.getCatsOfTheOwnerById(owner.getId());

        model.addAttribute("cats", cats);

        return "my_pets";
    }

    @GetMapping ("/new_cat")
    public String addCatToTheOwner(Model model) throws OwnerNotFoundException {
        Owner owner = ownerService.getCurrentOwner();
        model.addAttribute("currentOwner", owner);
        model.addAttribute("cat", new Cat());
        return "new_cat";
    }

    @PostMapping("/addCat")
    public String addCat(@RequestParam("name") String name,
                         @RequestParam("breed") Breed breed,
                         @RequestParam("ownerId") long ownerId) throws OwnerNotFoundException {

        catService.createCat(name, breed, ownerId);
        return "redirect:/my_pets";
    }
}