package com.thinh.foodnutrientfact.service;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;

public interface FoodNutriRepository {
     FoodInfoDTO getFoodNutri(String foodName);
}
