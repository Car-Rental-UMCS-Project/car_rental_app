package com.example.car_rental_app.repository.dao;

import com.example.car_rental_app.data.Car;
import com.example.car_rental_app.repository.dao.interfaces.ICarDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarDAO implements ICarDAO {

    @PersistenceContext
    private EntityManager entityManager;
    private final String GET_ALL_JPQL = "FROM com.example.car_rental_app.data.Car";
    private final String GET_BY_ID_JPQL = "SELECT c FROM com.example.car_rental_app.data.Car c WHERE c.id = :id";

    public CarDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Car> getById(Long id) {
        TypedQuery<Car> query = entityManager.createQuery(GET_BY_ID_JPQL, Car.class);
        query.setParameter("id", id);

        try {
            return Optional.ofNullable(query.getSingleResult());
        }catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Car> getAll() {
        TypedQuery<Car> query = entityManager.createQuery(GET_ALL_JPQL, Car.class);
        return query.getResultList();
    }

    @Override
    public void saveOrUpdate(Car car) {
        if(getById(car.getId()).isEmpty()) {
            entityManager.persist(car);
        }else {
            entityManager.merge(car);
        }
    }

    @Override
    public void delete(Long id) {
        getById(id).ifPresent(entityManager::remove);
    }
}
