package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;

import com.thinh.foodnutrientfact.MainActivity;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.service.FoodNutriRepository;

public class SqlFoodNutriRepositoryImp implements FoodNutriRepository {


    @Override
    public FoodInfoDTO getFoodNutri(String foodName, Context context) {
       DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.getFoodNutri(foodName);
        return null;
    }
}
