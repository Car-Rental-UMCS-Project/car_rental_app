package com.example.car_rental_app.data.records;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.Rental;
import com.example.car_rental_app.data.User;

public record CarRentalUserTuple(Car car, Rental rental, User user) {}
