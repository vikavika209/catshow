package com.vikavika209.catshow.controller;

import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.repository.OwnerRepository;
import com.vikavika209.catshow.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String registration() {
        return "registration";
    }

    /*@PostMapping("/submit_registration")
    public Owner createOwner(@RequestBody Owner owner){
        ownerService.createOwner();
        return owner;
    }*/

    @GetMapping("/enter")
    public String enter(){
        return "enter";
    }
}
