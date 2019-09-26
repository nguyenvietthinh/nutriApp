package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CalorieDailyService {
    private CalorieDailyRepository calorieDailyRepository;

    @Inject
    public CalorieDailyService(CalorieDailyRepository calorieDailyRepository) {
        this.calorieDailyRepository = calorieDailyRepository;
    }

    public boolean addCalDaily(CalorieDaily calorieDaily){
        return calorieDailyRepository.addCalDaily(calorieDaily);
    }
    public List<CalorieDaily> getCalorieDaily(Date date){
        return calorieDailyRepository.getCalorieDaily(date);
    }
}
