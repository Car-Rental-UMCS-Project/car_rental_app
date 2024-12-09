package com.example.car_rental_app.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class CustomerPanelController {

    @GetMapping("/customer")
    public String customerPanel(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "customer-panel";
    }
}
