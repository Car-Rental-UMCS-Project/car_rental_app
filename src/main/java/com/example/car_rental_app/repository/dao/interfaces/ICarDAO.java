package com.example.car_rental_app.repository.dao.interfaces;

import com.example.car_rental_app.data.Car;

import java.util.List;
import java.util.Optional;

public interface ICarDAO {
    Optional<Car> getById(Long id);
    List<Car> getAll();
    void saveOrUpdate(Car car);
    void delete(Long id);
}
