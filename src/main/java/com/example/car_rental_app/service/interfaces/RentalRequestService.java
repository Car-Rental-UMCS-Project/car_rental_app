package com.example.car_rental_app.service.interfaces;

import com.example.car_rental_app.data.RentalRequest;

import java.util.List;
import java.util.Optional;

public interface RentalRequestService {
    void saveOrUpdate(RentalRequest rentalrequest);

    Optional<RentalRequest> getById(Long id);

    Optional<RentalRequest> getRentalByCarId(Long carId);

    List<RentalRequest> getAll();

    void delete(Long id);

    void requestRent(Long carId, int days);
}
