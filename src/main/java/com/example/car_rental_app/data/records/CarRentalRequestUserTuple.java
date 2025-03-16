package com.example.car_rental_app.data.records;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.data.RentalRequest;
import com.example.car_rental_app.data.User;

public record CarRentalRequestUserTuple(Car car, RentalRequest rentalRequest, User user) {}
