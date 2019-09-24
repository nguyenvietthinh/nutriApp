package com.tma.techday.foodnutrientfact.service;

import android.app.Activity;

import javax.inject.Inject;

public class CalorieSettingService extends Activity {

    private CalorieSettingRepository calorieSettingRepository;

    @Inject
    public CalorieSettingService(CalorieSettingRepository calorieSettingRepository) {
        this.calorieSettingRepository = calorieSettingRepository;
    }

    public boolean insertCalorieSetting(Double amount){
      return calorieSettingRepository.insertCalorieSetting(amount);
    }
}
