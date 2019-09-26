package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;

import java.util.Date;
import java.util.List;

public interface CalorieDailyRepository {
    boolean addCalDaily(CalorieDaily calorieDaily);
    List<CalorieDaily> getCalorieDaily(Date date);
}
