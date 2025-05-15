package com.example.bookstore.service;

import com.example.bookstore.dto.UserRegistrationDto;
import com.example.bookstore.models.User;

public interface UserService {
    void registerUser(UserRegistrationDto userDto);
    User findByEmail(String email);
}

