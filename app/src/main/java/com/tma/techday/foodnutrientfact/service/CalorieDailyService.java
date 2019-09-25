package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;

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
}
