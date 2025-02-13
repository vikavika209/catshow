package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.service.AuthenticationService;
import com.vikavika209.catshow.service.CatService;
import com.vikavika209.catshow.service.OwnerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class OwnerController {
    private final AuthenticationService authenticationService;
    private final OwnerService ownerService;
    private final CatService catService;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(AuthenticationService authenticationService, OwnerService ownerService, CatService catService) {
        this.authenticationService = authenticationService;
        this.ownerService = ownerService;
        this.catService = catService;
    }

    private Owner getCurrentOwner() throws OwnerNotFoundException {
        return ownerService.getCurrentOwner();
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("owner", new Owner());
        return "registration";
    }

    @PostMapping("/submit_registration")
    public String registrationNewOwner(
            @ModelAttribute("owner") @Valid Owner owner,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            logger.warn("Ошибка при регистрации пользователя в OwnerController классе");
            return "registration";
        }
            authenticationService.signUp(owner.getName(), owner.getUsername(), owner.getPassword(), owner.getCity());

        return "registration-success";
        }

    @GetMapping("/my_show")
    public String getOwnerShows(Model model) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Owner owner = getCurrentOwner();
        Set<Show> shows = ownerService.getAllShowsByOwnerId(owner.getId());
        model.addAttribute("shows", shows);
        return "my_show";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) throws OwnerNotFoundException, ShowNotFoundException, CatNotFoundException {
        Owner owner = getCurrentOwner();

        String catNames = CollectionUtils.isEmpty(owner.getCats())
                ? "No cat has been found"
                : ownerService.getCatsOfTheOwnerById(owner.getId()).stream()
                .map(Cat::getName)
                .collect(Collectors.joining(", "));

        model.addAttribute("owner", owner);
        model.addAttribute("catNames", catNames);

        return "my_profile";
    }

    @GetMapping("/my_pets")
    public String getCatsByOwnerId(Model model) throws CatNotFoundException, OwnerNotFoundException {
        Owner owner = getCurrentOwner();
        Set<Cat> cats = ownerService.getCatsOfTheOwnerById(owner.getId());

        model.addAttribute("cats", cats);

        return "my_pets";
    }

    @GetMapping ("/new_cat")
    public String addCatToTheOwner(Model model) throws OwnerNotFoundException {
        Owner owner = getCurrentOwner();
        model.addAttribute("currentOwner", owner);
        model.addAttribute("cat", new Cat());
        return "new_cat";
    }

    @PostMapping("/addCat")
    public String addCatToTheCurrentOwner(@RequestParam("name") String name,
                         @RequestParam("breed") Breed breed) throws OwnerNotFoundException {

        Owner owner = getCurrentOwner();
        catService.createCat(name, breed, owner.getId());

        return "redirect:/my_pets";
    }
}