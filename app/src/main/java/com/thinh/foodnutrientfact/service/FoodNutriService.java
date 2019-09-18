package com.thinh.foodnutrientfact.service;

import android.app.Activity;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;

import javax.inject.Inject;

public class FoodNutriService extends Activity {

    private FoodNutriRepository foodNutriRepository;

    @Inject
    public FoodNutriService(FoodNutriRepository foodNutriRepository) {
        this.foodNutriRepository = foodNutriRepository;
    }

    public FoodInfoDTO getFoodNutri(String foodName){
        FoodInfoDTO foodNutri = foodNutriRepository.getFoodNutri(foodName);
        if(foodNutri==null){
            return null;
        }
        else{
            return foodNutri;
        }
    }
}
