package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.service.interfaces.CarService;
import com.example.car_rental_app.service.interfaces.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class RentalController {
    private final RentalService rentalService;
    private final CarService carService;

    public RentalController(RentalService rentalService, CarService carService) {
        this.rentalService = rentalService;
        this.carService = carService;
    }
    @GetMapping("/rent/{id}")
    public String rentCarDetails(@PathVariable Long id, Model model) {
        Car car = carService.getById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        model.addAttribute("car", car);
        return "rent-details";
    }
    @PostMapping("/rent")
    public String rentCar(@RequestParam Long carId,
                          @RequestParam int hours,
                          RedirectAttributes redirectAttributes) {
        try {
            rentalService.rentCar(carId, hours);
            redirectAttributes.addFlashAttribute("message", "Car Rented");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error renting car: " + e.getMessage());
        }
        return "redirect:/cars/customer/all";
    }
}
