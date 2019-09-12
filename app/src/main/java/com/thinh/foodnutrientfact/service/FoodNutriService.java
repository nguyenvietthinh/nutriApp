package com.thinh.foodnutrientfact.service;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;

import javax.inject.Inject;

public class FoodNutriService {

    private FoodNutriRepository foodNutriRepository;

    @Inject
    public FoodNutriService(FoodNutriRepository foodNutriRepository) {
        this.foodNutriRepository = foodNutriRepository;
    }

    public FoodInfoDTO getFoodNutri(String foodName){

        FoodInfoDTO foodInfoDTO = foodNutriRepository.getFoodNutri(foodName);
        return foodInfoDTO;
    }
}
