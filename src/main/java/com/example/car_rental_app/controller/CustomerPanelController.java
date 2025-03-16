package com.example.car_rental_app.controller;

import com.example.car_rental_app.data.records.CarRentalRequestTuple;
import com.example.car_rental_app.data.records.CarRentalTuple;
import com.example.car_rental_app.repository.dao.interfaces.IRentalDAO;
import com.example.car_rental_app.service.interfaces.CarService;
import com.example.car_rental_app.service.interfaces.RentalRequestService;
import com.example.car_rental_app.service.interfaces.RentalService;
import com.example.car_rental_app.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerPanelController {

    private final UserService userService;
    private final IRentalDAO rentalDAO;
    private final CarService carService;
    private final RentalRequestService rentalRequestService;
    private final RentalService rentalService;

    public CustomerPanelController(UserService userService, IRentalDAO rentalDAO,
                                   CarService carService, RentalRequestService rentalRequestService,
                                   RentalService rentalService) {
        this.userService = userService;
        this.rentalDAO = rentalDAO;
        this.carService = carService;
        this.rentalRequestService = rentalRequestService;
        this.rentalService = rentalService;
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

        List<CarRentalTuple> carRentalTuples = new ArrayList<>();

        List<CarRentalRequestTuple> carRentalRequestTuples = new ArrayList<>();

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
                .stream()
                .filter(rental ->
                        Objects.equals(rental.getUserId(), userService.getCurrentUser().getId()))
                .forEach(rental ->
                        carRentalTuples.add(new CarRentalTuple(
                                carService.getById(rental.getCarId()).orElseThrow(), rental)
                        )
                );

        rentalRequestService.getAll()
                .stream()
                .filter(rentalRequest ->
                        Objects.equals(rentalRequest.getUserId(), userService.getCurrentUser().getId()))
                .forEach(rentalRequest ->
                        carRentalRequestTuples.add(new CarRentalRequestTuple(
                                carService.getById(rentalRequest.getCarId()).orElseThrow(), rentalRequest
                        ))
                );

        model.addAttribute("carRentalTuples", carRentalTuples);
        model.addAttribute("carRentalRequestTuples", carRentalRequestTuples);
        return "customer-rented-cars";
    }
}
