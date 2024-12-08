package com.example.car_rental_app.service.interfaces;

import com.example.car_rental_app.data.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    void saveOrUpdate(Car car);
    Optional<Car> getById(Long id);
    List<Car> getAll();
    void delete(Long id);
}
