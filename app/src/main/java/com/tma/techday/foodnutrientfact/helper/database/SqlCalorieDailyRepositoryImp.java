package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.service.CalorieDailyRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SqlCalorieDailyRepositoryImp implements CalorieDailyRepository {

    private CalorieDailyDAO calorieDailyDAO;

    @Inject
    public SqlCalorieDailyRepositoryImp(CalorieDailyDAO calorieDailyDAO) {
        this.calorieDailyDAO = calorieDailyDAO;
    }

    @Override
    public boolean addCalDaily(CalorieDaily calorieDaily) {
        return calorieDailyDAO.addCalDaily(calorieDaily);
    }

    @Override
    public List<CalorieDaily> getCalorieDaily(Date date) {
        return calorieDailyDAO.getCalorieDaily(date);
    }
}
