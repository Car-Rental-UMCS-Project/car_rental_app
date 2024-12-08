package com.example.car_rental_app.service;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.repository.dao.interfaces.ICarDAO;
import com.example.car_rental_app.service.interfaces.CarService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final ICarDAO carDAO;

    public CarServiceImpl(ICarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Override
    public void saveOrUpdate(Car car) {
        carDAO.saveOrUpdate(car);
    }

    @Override
    public Optional<Car> getById(Long id) {
       return carDAO.getById(id);
    }

    @Override
    public List<Car> getAll() {
        return carDAO.getAll();
    }

    @Override
    public void delete(Long id) {
        carDAO.delete(id);
    }
}
