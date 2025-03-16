package com.example.car_rental_app.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long carId;

    private int timeInDays;

    private int totalCost;

    private LocalDate rentalDate;

    private LocalDate returnDate;

    private int lateFee = 0;
}
