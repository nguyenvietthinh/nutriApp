package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.CalorieSetting;

public interface CalorieSettingRepository {
    boolean insertCalorieSetting(CalorieSetting calorieSetting);
    CalorieSetting getCalorieSetting();
}
