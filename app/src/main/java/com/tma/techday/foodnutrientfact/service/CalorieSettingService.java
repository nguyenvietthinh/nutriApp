package com.tma.techday.foodnutrientfact.service;

import android.app.Activity;

import com.tma.techday.foodnutrientfact.model.CalorieSetting;

import java.util.Date;

import javax.inject.Inject;

public class CalorieSettingService extends Activity {

    private CalorieSettingRepository calorieSettingRepository;

    @Inject
    public CalorieSettingService(CalorieSettingRepository calorieSettingRepository) {
        this.calorieSettingRepository = calorieSettingRepository;
    }

    public boolean insertCalorieSetting(CalorieSetting calorieSetting){
      return calorieSettingRepository.insertCalorieSetting(calorieSetting);
    }

    public CalorieSetting getCalorieSetting(){
        return calorieSettingRepository.getCalorieSetting();
    }

    public void updateCalorieSetting(CalorieSetting  calorieSetting){
        calorieSettingRepository.updateCalorieSetting (calorieSetting);
    }
}
