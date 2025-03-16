package com.example.car_rental_app.service;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.RentalRequest;
import com.example.car_rental_app.data.User;
import com.example.car_rental_app.repository.dao.CarDAO;
import com.example.car_rental_app.repository.dao.interfaces.IRentalRequestDAO;
import com.example.car_rental_app.service.interfaces.RentalRequestService;
import com.example.car_rental_app.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalRequestServiceImpl implements RentalRequestService {

    private final IRentalRequestDAO rentalRequestDAO;
    private final UserService userService;
    private final CarDAO carDAO;

    public RentalRequestServiceImpl(IRentalRequestDAO rentalRequestDAO, UserService userService,
                                    CarDAO carDAO) {
        this.rentalRequestDAO = rentalRequestDAO;
        this.userService = userService;
        this.carDAO = carDAO;
    }

    @Override
    public void saveOrUpdate(RentalRequest rentalrequest) {
        rentalRequestDAO.saveOrUpdate(rentalrequest);
    }

    @Override
    public Optional<RentalRequest> getById(Long id) {
        return rentalRequestDAO.getById(id);
    }

    @Override
    public Optional<RentalRequest> getRentalByCarId(Long carId) {
        return rentalRequestDAO.getRentalRequestByCarId(carId);
    }

    @Override
    public List<RentalRequest> getAll() {
        return rentalRequestDAO.getAll();
    }

    @Override
    public void delete(Long id) {
        rentalRequestDAO.delete(id);
    }

    @Override
    public void requestRent(Long carId, int days) {
        User user = userService.getCurrentUser();
        Car car = carDAO.getById(carId).orElseThrow();

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setCarId(carId);
        rentalRequest.setUserId(user.getId());
        rentalRequest.setTimeInDays(days);
        rentalRequest.setTotalCost(days * car.getPricePerDay());

        rentalRequestDAO.saveOrUpdate(rentalRequest);
    }
}
