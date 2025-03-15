package com.example.car_rental_app.repository.dao;

import com.example.car_rental_app.data.RentalRequest;
import com.example.car_rental_app.repository.dao.interfaces.IRentalRequestDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RentalRequestDAO implements IRentalRequestDAO {

    private final String GET_ALL_JPQL = "FROM com.example.car_rental_app.data.RentalRequest";
    private final String GET_BY_ID_JPQL = "SELECT rr FROM com.example.car_rental_app.data.RentalRequest rr WHERE rr.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    public RentalRequestDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<RentalRequest> getById(Long id) {
        TypedQuery<RentalRequest> query = entityManager.createQuery(GET_BY_ID_JPQL, RentalRequest.class);
        query.setParameter("id", id);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<RentalRequest> getAll() {
        TypedQuery<RentalRequest> query = entityManager.createQuery(GET_ALL_JPQL, RentalRequest.class);
        return query.getResultList();
    }

    @Override
    public void saveOrUpdate(RentalRequest rentalRequest) {
        if(getById(rentalRequest.getId()).isEmpty()) {
            entityManager.persist(rentalRequest);
        }else {
            entityManager.merge(rentalRequest);
        }
    }

    @Override
    public void delete(Long id) {
        getById(id).ifPresent(entityManager::remove);
    }
}
