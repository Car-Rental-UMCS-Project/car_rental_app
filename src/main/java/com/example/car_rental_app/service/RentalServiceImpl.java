package com.example.car_rental_app.service;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.Rental;
import com.example.car_rental_app.data.RentalRequest;
import com.example.car_rental_app.repository.dao.interfaces.ICarDAO;
import com.example.car_rental_app.repository.dao.interfaces.IRentalDAO;
import com.example.car_rental_app.service.interfaces.RentalService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final IRentalDAO rentalDAO;
    private final ICarDAO carDAO;
    private final UserServiceImpl userService;
    private final RentalRequestServiceImpl rentalRequestService;

    public RentalServiceImpl(IRentalDAO rentalDAO, ICarDAO carDAO, UserServiceImpl userService,
                             RentalRequestServiceImpl rentalRequestService) {
        this.rentalDAO = rentalDAO;
        this.carDAO = carDAO;
        this.userService = userService;
        this.rentalRequestService = rentalRequestService;
    }

    @Override
    public void rentCar(Long carId, int days) {
        //User user = userService.getCurrentUser();
        RentalRequest rentalRequest = rentalRequestService.getRentalByCarId(carId).orElseThrow();

        Car car = carDAO.getById(carId).orElseThrow();
        if (car.getIsRented()) {
            throw new IllegalArgumentException("Car is already rented");
        }
        car.setIsRented(true);

        Rental rental = new Rental();
        rental.setUserId(rentalRequest.getUserId());
        rental.setCarId(car.getId());
        rental.setTimeInDays(days);
        rental.setTotalCost(days * car.getPricePerDay());
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(days));

        rentalRequestService.delete(rentalRequest.getId());
        rentalDAO.saveRental(rental);
        carDAO.saveOrUpdate(car);
    }

    @Override
    public void returnCar(Long carId) {
        Rental rental = rentalDAO.getRentalByCarId(carId).orElseThrow(() ->
                new IllegalArgumentException("Rental not found"));

        Car car = carDAO.getById(carId).orElseThrow();
        if (!car.getIsRented()) {
            return;
        }
        car.setIsRented(false);

        carDAO.saveOrUpdate(car);
        this.rentalDAO.delete(rental.getId());
    }

    public void updateLateFee(Long rentalId, int fee) {
        Rental rental = rentalDAO.getRentalById(rentalId).orElseThrow();

        rental.setLateFee(fee);
        rentalDAO.saveRental(rental);
    }
}
