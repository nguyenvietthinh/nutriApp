package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.CalorieSetting;
import com.tma.techday.foodnutrientfact.service.CalorieSettingRepository;

import javax.inject.Inject;

public class SqlCalorieSettingRepositoryImp implements CalorieSettingRepository {

    private CalorieSettingDAO calorieSettingDAO;

    @Inject
    public SqlCalorieSettingRepositoryImp(CalorieSettingDAO calorieSettingDAO) {
        this.calorieSettingDAO = calorieSettingDAO;
    }

    @Override
    public boolean insertCalorieSetting(CalorieSetting calorieSetting) {
       return calorieSettingDAO.insertCalorieSetting(calorieSetting);
    }

    @Override
    public CalorieSetting getCalorieSetting() {
        return calorieSettingDAO.getCalorieSetting();
    }
}
