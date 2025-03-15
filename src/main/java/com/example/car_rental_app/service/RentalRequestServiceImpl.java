package com.example.car_rental_app.service;

import com.example.car_rental_app.data.RentalRequest;
import com.example.car_rental_app.repository.dao.interfaces.IRentalRequestDAO;
import com.example.car_rental_app.service.interfaces.RentalRequestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalRequestServiceImpl implements RentalRequestService {

    private final IRentalRequestDAO rentalRequestDAO;

    public RentalRequestServiceImpl(IRentalRequestDAO rentalRequestDAO) {
        this.rentalRequestDAO = rentalRequestDAO;
    }

    @Override
    public void saveOrUpdate(RentalRequest rentalrequest) {
        rentalRequestDAO.saveOrUpdate(rentalrequest);
    }

    @Override
    public Optional<RentalRequest> getById(Long id) {
        return rentalRequestDAO.getById(id);
    }

    @Override
    public List<RentalRequest> getAll() {
        return rentalRequestDAO.getAll();
    }

    @Override
    public void delete(Long id) {
        rentalRequestDAO.delete(id);
    }
}
