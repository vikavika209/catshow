package com.vikavika209.catshow.controller;


import com.vikavika209.catshow.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final OwnerService ownerService;

    @Autowired
    public AdminController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/admin")
    public String allOwners(Model model) {
        model.addAttribute("owners", ownerService.getAllOwners());
        return "admin";
    }


}
