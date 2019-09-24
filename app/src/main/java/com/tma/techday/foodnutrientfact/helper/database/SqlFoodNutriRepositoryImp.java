package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.service.FoodNutriRepository;

import javax.inject.Inject;

public class SqlFoodNutriRepositoryImp implements FoodNutriRepository {

    private FoodNutriDAO foodNutriDAO;

    @Inject
    public SqlFoodNutriRepositoryImp(FoodNutriDAO foodNutriDAO) {
        this.foodNutriDAO = foodNutriDAO;
    }

    @Override
    public FoodInfoDTO getFoodNutri(String foodName) {
        return foodNutriDAO.getFoodNutri(foodName);
    }


}
