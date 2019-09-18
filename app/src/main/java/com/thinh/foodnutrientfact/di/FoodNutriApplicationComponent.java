package com.thinh.foodnutrientfact.di;

import com.thinh.foodnutrientfact.activity.AddToCartActivity;
import com.thinh.foodnutrientfact.activity.DetectActivity;
import com.thinh.foodnutrientfact.activity.MainActivity;
import com.thinh.foodnutrientfact.activity.SettingActivity;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = { FoodNutriApplicationModule.class, ServiceModule.class,DatabaseModule.class})
public interface FoodNutriApplicationComponent  {
    void inject(MainActivity target);

    void inject(DetectActivity detectActivity);

    void inject(AddToCartActivity addToCartActivity);

    void inject(SettingActivity settingActivity);
}