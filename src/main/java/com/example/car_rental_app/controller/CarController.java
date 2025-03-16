package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.records.CarRentalRequestUserTuple;
import com.example.car_rental_app.data.records.CarRentalUserTuple;
import com.example.car_rental_app.repository.dao.RentalDAO;
import com.example.car_rental_app.service.interfaces.CarService;
import com.example.car_rental_app.service.interfaces.RentalRequestService;
import com.example.car_rental_app.service.interfaces.RentalService;
import com.example.car_rental_app.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final RentalDAO rentalDAO;
    private final RentalRequestService rentalRequestService;
    private final UserService userService;
    private final RentalService rentalService;

    public CarController(CarService carService, RentalDAO rentalDao,
                         RentalRequestService rentalRequestService,
                         UserService userService, RentalService rentalService) {
        this.carService = carService;
        this.rentalDAO = rentalDao;
        this.rentalRequestService = rentalRequestService;
        this.userService = userService;
        this.rentalService = rentalService;
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
        List<CarRentalRequestUserTuple> carRentalRequestUserTuples = new ArrayList<>();
        List<CarRentalUserTuple> carRentalUserTuples = new ArrayList<>();

        rentalRequestService.getAll()
                .forEach(rentalRequest ->
                        carRentalRequestUserTuples.add(new CarRentalRequestUserTuple(
                                carService.getById(rentalRequest.getCarId()).orElseThrow(), rentalRequest,
                                userService.findUserById(rentalRequest.getUserId()).orElseThrow()
                        ))
                );

        //W tym miejscu nieco łamiemy konwencje genewskie, ale kto by się tym przejmował skoro działa
        rentalDAO.getAllRentals()
                .forEach(rental -> {
                    LocalDate returnDate = rental.getReturnDate();
                    LocalDate currentDate = LocalDate.now();

                    if (currentDate.isAfter(returnDate)) {
                        rentalService.updateLateFee(rental.getId(),
                                rental.getLateFee() + (int) (500
                                                * ChronoUnit.DAYS.between(currentDate, returnDate)));
                    }

                });

        rentalDAO.getAllRentals()
                .forEach(rental ->
                        carRentalUserTuples.add(new CarRentalUserTuple(
                                carService.getById(rental.getCarId()).orElseThrow(),
                                rental,
                                userService.findUserById(rental.getUserId()).orElseThrow()
                        ))
                );

        model.addAttribute("cars", carService.getAll());
        model.addAttribute("carRentalUserTuples", carRentalUserTuples);
        model.addAttribute("carRentalRequestUserTuples", carRentalRequestUserTuples);
        return "available-cars-admin";
    }

    @GetMapping("/customer/all")
    public String allCarsCustomer(Model model) {
        model.addAttribute("availableCars", getSpecificCars(false));
        model.addAttribute("rentedCars", getSpecificCars(true));
        return "available-cars-customer";
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
