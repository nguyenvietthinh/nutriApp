package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.User;


public interface UserRepository {
    boolean insertUser(User user);
    User getUser();
}
