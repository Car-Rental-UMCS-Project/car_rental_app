package com.example.car_rental_app.service;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.Rental;
import com.example.car_rental_app.data.User;
import com.example.car_rental_app.repository.dao.interfaces.ICarDAO;
import com.example.car_rental_app.repository.dao.interfaces.IRentalDAO;
import com.example.car_rental_app.service.interfaces.RentalService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final IRentalDAO rentalDAO;
    private final ICarDAO carDAO;
    private final UserServiceImpl userService;

    public RentalServiceImpl(IRentalDAO rentalDAO, ICarDAO carDAO, UserServiceImpl userService) {
        this.rentalDAO = rentalDAO;
        this.carDAO = carDAO;
        this.userService = userService;
    }

    @Override
    public void rentCar(Long carId, int days) {
        User user = userService.getCurrentUser();
        Car car = carDAO.getById(carId).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        if (car.getIsRented()){
            throw new IllegalArgumentException("Car is already rented");
        }
        car.setIsRented(true);

        Rental rental = new Rental();
        rental.setUserId(user.getId());
        rental.setCarId(car.getId());
        rental.setTimeInDays(days);
        rental.setTotalCost(days * car.getPricePerDay());
        rentalDAO.saveRental(rental);
        carDAO.saveOrUpdate(car);
    }
}
