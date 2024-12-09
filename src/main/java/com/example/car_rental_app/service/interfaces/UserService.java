package com.example.car_rental_app.service.interfaces;

import com.example.car_rental_app.data.User;
import com.example.car_rental_app.data.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDTO userDto);
    User findUserByUsername(String username);
    List<UserDTO> findAllUsers();
    Optional<User> findUserById(Long id);
}