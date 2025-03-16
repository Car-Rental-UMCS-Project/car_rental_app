package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.service.interfaces.CarService;
import com.example.car_rental_app.service.interfaces.RentalRequestService;
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
    private final RentalRequestService rentalRequestService;
    private final CarService carService;

    public RentalController(RentalService rentalService, CarService carService,
                            RentalRequestService rentalRequestService) {
        this.rentalService = rentalService;
        this.carService = carService;
        this.rentalRequestService = rentalRequestService;
    }

    @GetMapping("/request-rental/{id}")
    public String requestRent(@PathVariable Long id, Model model) {
        Car car = carService.getById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        model.addAttribute("car", car);
        return "request-rental-details";
    }

    @PostMapping("/request-rental")
    public String requestRent(@RequestParam Long carId, @RequestParam int days, Model model) {
        rentalRequestService.requestRent(carId, days);
        return "redirect:/cars/customer/all";
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
                          @RequestParam int days,
                          RedirectAttributes redirectAttributes) {
        try {
            rentalService.rentCar(carId, days);
            redirectAttributes.addFlashAttribute("message", "Car Rented");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error renting car: " + e.getMessage());
        }
        return "redirect:/cars/admin/all";
    }

    @PostMapping("/return")
    public String returnCar(@RequestParam Long carId) {
        rentalService.returnCar(carId);
        return "redirect:/cars/admin/all";
    }
}
