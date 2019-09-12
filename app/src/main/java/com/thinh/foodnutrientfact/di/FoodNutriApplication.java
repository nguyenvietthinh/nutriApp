package com.thinh.foodnutrientfact.di;

import android.app.Application;

public class FoodNutriApplication extends Application {

    private FNServiceComponent fnServiceComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        fnServiceComponent = DaggerFNServiceComponent.builder().serviceModule(new ServiceModule()).build();

    }

    public FNServiceComponent getComponent() {
        return fnServiceComponent;
    }}
