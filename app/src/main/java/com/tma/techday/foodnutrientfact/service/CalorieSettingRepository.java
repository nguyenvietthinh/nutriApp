package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.CalorieSetting;

import java.util.Date;

public interface CalorieSettingRepository {
    boolean insertCalorieSetting(CalorieSetting calorieSetting);
    CalorieSetting getCalorieSetting();
    void updateCalorieSetting(CalorieSetting calorieSetting);
}
