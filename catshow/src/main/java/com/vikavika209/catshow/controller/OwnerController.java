package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class OwnerController {
    private OwnerService ownerService;

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
            return "registration-success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "owner-success";
    }

    @GetMapping("/enter")
    public String enter(){
        return "enter";
    }
}
