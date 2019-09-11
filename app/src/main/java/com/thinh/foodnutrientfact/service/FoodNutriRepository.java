package com.thinh.foodnutrientfact.service;

import android.content.Context;
import android.database.Cursor;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;

public interface FoodNutriRepository {

     FoodInfoDTO getFoodNutri(String foodName, Context context);
}
