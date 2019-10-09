package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.User;

import javax.inject.Inject;

public class UserService {

    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean insertUser(User user){
        return userRepository.insertUser(user);
    }
    public User getUser(){
        return userRepository.getUser();
    }
}
