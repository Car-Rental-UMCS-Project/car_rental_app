package com.example.car_rental_app.service.interfaces;

public interface RentalService {
    void rentCar(Long carId, int hours);
    void returnCar(Long carId);
    void updateLateFee(Long rentalId, int fee);
}
