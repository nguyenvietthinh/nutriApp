package com.thinh.foodnutrientfact.helper.database;

import com.thinh.foodnutrientfact.service.CalorieSettingRepository;

import javax.inject.Inject;

public class SqlCalorieSettingRepositoryImp implements CalorieSettingRepository {

    private CalorieSettingDAO calorieSettingDAO;

    @Inject
    public SqlCalorieSettingRepositoryImp(CalorieSettingDAO calorieSettingDAO) {
        this.calorieSettingDAO = calorieSettingDAO;
    }

    @Override
    public boolean insertCalorieSetting(Double amount) {
       return calorieSettingDAO.insertCalorieSetting(amount);
    }
}
