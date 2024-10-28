package com.doma.artserver.controller;

import com.doma.artserver.service.MuseumExhibitionFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final MuseumExhibitionFacade museumExhibitionFacade;

    public AdminController(MuseumExhibitionFacade museumExhibitionFacade) {
        this.museumExhibitionFacade = museumExhibitionFacade;
    }

    @GetMapping("/loadData")
    public String loadData() {
        museumExhibitionFacade.loadData();
        return "Data loaded successfully";
    }

    @GetMapping("/updateData")
    public String updateData() {
        museumExhibitionFacade.updateData();
        return "Data updated successfully";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}