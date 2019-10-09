package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.model.User;
import com.tma.techday.foodnutrientfact.service.UserRepository;

import javax.inject.Inject;

public class SqlUserRepositoryImp implements UserRepository {

    private UserDAO userDAO;

    @Inject
    public SqlUserRepositoryImp(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean insertUser(User user) {
        return userDAO.insertUser(user);
    }

    @Override
    public User getUser() {
        return userDAO.getUser();
    }
}
