package com.example.car_rental_app.repository.dao;

import com.example.car_rental_app.data.Rental;
import com.example.car_rental_app.repository.dao.interfaces.IRentalDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RentalDAO implements IRentalDAO {

    private final String GET_ALL_JPQL = "FROM com.example.car_rental_app.data.Rental";
    private final String GET_BY_ID_JPQL = "SELECT r FROM com.example.car_rental_app.data.Rental r WHERE r.id = :id";
    @PersistenceContext
    private EntityManager entityManager;

    public RentalDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Rental> getRentalById(Long id) {
        TypedQuery<Rental> query = entityManager.createQuery(GET_BY_ID_JPQL, Rental.class);
        query.setParameter("id", id);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Rental> getAllRentals() {
        TypedQuery<Rental> query = entityManager.createQuery(GET_ALL_JPQL, Rental.class);
        return query.getResultList();
    }

    @Override
    public Optional<Rental> getRentalByCarId(Long carId) {
        List<Rental> allRentals = getAllRentals();

        return allRentals.stream().filter(rental -> rental.getCarId().equals(carId)).findFirst();
    }

    @Override
    public void saveRental(Rental rental) {
        entityManager.persist(rental);
    }

    @Override
    public void delete(Long id) {
        getRentalById(id).ifPresent(entityManager::remove);
    }
}
