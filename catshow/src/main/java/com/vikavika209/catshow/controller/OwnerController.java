package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.service.OwnerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/")
public class OwnerController {
    private OwnerService ownerService;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
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

    @GetMapping("/enter")
    public String enter(){
        return "enter";
    }

    @PostMapping("/submit_login")
    public String showHomePageAfterLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {
        try {
            Owner verifyOwner = ownerService.verifyOwner(email, password);
            if (verifyOwner != null) {
                logger.info("Пользователь с email: {} успешно найден", email);
                model.addAttribute("ownerId", verifyOwner.getId());
                return "home_page";
            } else {
                model.addAttribute("error", "Неверный email или пароль");
                return "enter";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при входе: " + e.getMessage());
            return "enter";
        }
    }

    @GetMapping("/my_show/{id}")
    public String getOwnerShows(@PathVariable("id") long ownerId, Model model) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        Set<Show> shows = ownerService.getAllShowsByOwnerId(ownerId);
        model.addAttribute("shows", shows);
        model.addAttribute("ownerId", ownerId);
        return "my_show";
    }

    @GetMapping("/profile/{id}")
    public String getOwnerData(@PathVariable("id") long ownerId, Model model) throws OwnerNotFoundException {
        Owner owner = ownerService.getOwner(ownerId);

        model.addAttribute("owner", owner);

        return "my_profile";
    }


    @GetMapping("/success")
    public String showSuccessPage() {
        return "registration-success";
    }


}