package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;

public interface FoodNutriRepository {
     FoodInfoDTO getFoodNutri(String foodName);
}
