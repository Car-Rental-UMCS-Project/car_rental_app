package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.service.interfaces.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/add")
    public String addCarGet(@ModelAttribute Car car) {
        return "car";
    }

    @PostMapping("/add")
    public String addCarPost(@ModelAttribute Car car) {
        carService.saveOrUpdate(car);
        return "redirect:/cars/admin/all";
    }

    @GetMapping("/update/{id}")
    public String updateCarGet(@PathVariable Long id, Model model) {
        Car car = carService.getById(id).orElse(null);
        model.addAttribute("car", car);
        return "car";
    }

    @PostMapping("/update/{id}")
    public String updateCarPost(@ModelAttribute Car car) {
        carService.saveOrUpdate(car);
        return "redirect:/cars/admin/all";
    }

    @GetMapping("/admin/all")
    public String allCarsAdmin(Model model) {
        model.addAttribute("cars", carService.getAll());
        return "cars-admin";
    }

    @GetMapping("/customer/all")
    public String allCarsCustomer(Model model) {
        model.addAttribute("availableCars", getSpecificCars(false));
        model.addAttribute("rentedCars", getSpecificCars(true));

        return "cars-customer";
    }

    @PostMapping("/delete")
    public String deleteCar(@RequestParam Long id) {
        carService.delete(id);
        return "redirect:/cars/admin/all";
    }

    private List<Car> getSpecificCars(boolean rented) {
        return carService.getAll()
                .stream()
                .filter(car -> car.getIsRented() == rented)
                .collect(Collectors.toList());
    }
}
