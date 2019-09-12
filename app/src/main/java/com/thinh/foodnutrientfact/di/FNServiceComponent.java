package com.thinh.foodnutrientfact.di;


import com.thinh.foodnutrientfact.MainActivity;
import com.thinh.foodnutrientfact.service.FoodNutriRepository;
import com.thinh.foodnutrientfact.service.FoodNutriService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class})

public interface FNServiceComponent {

    void inject(MainActivity mainActivity);

}
