package com.example.car_rental_app.repository.dao.interfaces;

import com.example.car_rental_app.data.Rental;

import java.util.List;
import java.util.Optional;

public interface IRentalDAO {
    Optional<Rental> getRentalById(Long id);
    List<Rental> getAllRentals();
    void saveRental(Rental rental);
    void delete(Long id);

}
