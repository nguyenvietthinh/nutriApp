package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.service.FoodNutriRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SqlFoodNutriRepositoryImp implements FoodNutriRepository {

    private  FoodNutriDAO foodNutriDAO;

    @Inject
    public SqlFoodNutriRepositoryImp(FoodNutriDAO foodNutriDAO) {
        this.foodNutriDAO = foodNutriDAO;
    }



    @Override
    public FoodInfoDTO getFoodNutri(String foodName) {

        return foodNutriDAO.getFoodNutri(foodName);
    }


}
