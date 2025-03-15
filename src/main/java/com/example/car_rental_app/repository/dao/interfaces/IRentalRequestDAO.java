package com.example.car_rental_app.repository.dao.interfaces;

import com.example.car_rental_app.data.RentalRequest;

import java.util.List;
import java.util.Optional;

public interface IRentalRequestDAO {
    Optional<RentalRequest> getById(Long id);

    List<RentalRequest> getAll();

    void saveOrUpdate(RentalRequest rentalRequest);

    void delete(Long id);
}
