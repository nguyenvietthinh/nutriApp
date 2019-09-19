package com.thinh.foodnutrientfact.model;

public class Order {
    private String foodName;
    private double calorieAmount;
    private double foodWeight;

    public Order(String foodName, double calorieAmount, double foodWeight) {
        this.foodName = foodName;
        this.calorieAmount = calorieAmount;
        this.foodWeight = foodWeight;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCalorieAmount(double calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public void setFoodWeight(double foodWeight) {
        this.foodWeight = foodWeight;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getCalorieAmount() {
        return calorieAmount;
    }

    public double getFoodWeight() {
        return foodWeight;
    }
}
