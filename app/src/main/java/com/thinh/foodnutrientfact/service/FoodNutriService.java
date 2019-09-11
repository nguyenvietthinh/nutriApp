package com.thinh.foodnutrientfact.service;

import android.content.Context;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;

public class FoodNutriService {

    private FoodNutriRepository foodNutriRepository;

    public FoodNutriService() {
    }

    public FoodNutriService(FoodNutriRepository foodNutriRepository) {
        this.foodNutriRepository = foodNutriRepository;
    }

    public FoodInfoDTO getFoodNutri(String foodName,Context context){

        FoodInfoDTO foodInfoDTO = foodNutriRepository.getFoodNutri(foodName,context);
        return foodInfoDTO;
    }
}
