package com.thinh.foodnutrientfact.di;

import com.thinh.foodnutrientfact.helper.database.SqlFoodNutriRepositoryImp;
import com.thinh.foodnutrientfact.service.FoodNutriRepository;
import com.thinh.foodnutrientfact.service.FoodNutriService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {



    @Provides @Singleton
    FoodNutriRepository foodNutriRepository (SqlFoodNutriRepositoryImp sqlFoodNutriRepositoryImp){
        return sqlFoodNutriRepositoryImp;
    }

    @Provides @Singleton
    FoodNutriService provideFoodNutriService(FoodNutriRepository foodNutriRepository){
        return new FoodNutriService(foodNutriRepository);
    }

}
