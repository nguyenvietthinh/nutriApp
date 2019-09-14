package com.thinh.foodnutrientfact.di;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class FoodNutriApplication extends Application implements HasActivityInjector {

    protected FoodNutriApplicationComponent mApplicationComponent;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerFoodNutriApplicationComponent
                .builder()
                .foodNutriApplicationModule(new FoodNutriApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();
    }

    public FoodNutriApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
