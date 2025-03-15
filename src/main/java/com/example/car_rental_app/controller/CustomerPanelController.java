package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.CarRentalPair;
import com.example.car_rental_app.repository.dao.interfaces.IRentalDAO;
import com.example.car_rental_app.service.interfaces.CarService;
import com.example.car_rental_app.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerPanelController {

    private final UserService userService;
    private final IRentalDAO rentalDAO;
    private final CarService carService;

    public CustomerPanelController(UserService userService, IRentalDAO rentalDAO,
                                   CarService carService) {
        this.userService = userService;
        this.rentalDAO = rentalDAO;
        this.carService = carService;
    }

    @GetMapping("/customer")
    public String customerPanel(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "customer-panel";
    }

    @GetMapping("/customer/cars")
    public String customerCars(Model model) {

        List<CarRentalPair> carRentalPairs = new ArrayList<>();

        rentalDAO.getAllRentals()
                .stream()
                .filter(rental ->
                        Objects.equals(rental.getUserId(), userService.getCurrentUser().getId()))
                .forEach(rental ->
                        carRentalPairs.add(new CarRentalPair(
                                carService.getById(rental.getCarId()).orElseThrow(), rental)
                        )
                );

        model.addAttribute("carRentalPairs", carRentalPairs);
        return "customer-rented-cars";
    }
}
