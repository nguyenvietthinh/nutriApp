package com.tma.techday.foodnutrientfact.di;

import com.tma.techday.foodnutrientfact.activity.AddToCartActivity;
import com.tma.techday.foodnutrientfact.activity.CalorieComparisionActivity;
import com.tma.techday.foodnutrientfact.activity.DetectActivity;
import com.tma.techday.foodnutrientfact.activity.DetectRealTimeActivity;
import com.tma.techday.foodnutrientfact.activity.EditUserActivity;
import com.tma.techday.foodnutrientfact.activity.MainActivity;
import com.tma.techday.foodnutrientfact.activity.SettingActivity;
import com.tma.techday.foodnutrientfact.activity.UserActivity;
import com.tma.techday.foodnutrientfact.gui.dialog.FoodNutriResultDialog;
import com.tma.techday.foodnutrientfact.gui.dialog.NutritionComponentStatisticsDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { FoodNutriApplicationModule.class, ServiceModule.class,DatabaseModule.class})
public interface FoodNutriApplicationComponent  {

    void inject(MainActivity target);

    void inject(DetectActivity detectActivity);

    void inject(AddToCartActivity addToCartActivity);

    void inject(SettingActivity settingActivity);

    void inject(FoodNutriResultDialog foodNutriResultDialog);

    void inject(CalorieComparisionActivity calorieComparisionActivity);

    void inject(EditUserActivity editUserActivity);

    void inject(UserActivity userActivity);

    void inject(DetectRealTimeActivity detectRealTimeActivity);

    void inject(NutritionComponentStatisticsDialog nutritionComponentStatisticsDialog);
}