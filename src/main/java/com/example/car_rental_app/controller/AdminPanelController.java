package com.example.car_rental_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {
    @GetMapping("/admin")
    public String adminPanel() {
        return "admin-panel";
    }
}
