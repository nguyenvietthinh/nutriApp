package com.tma.techday.foodnutrientfact.model;

/**
 * Detail order in cart used to save data to SharePreference
 */
public class Order {
    private String foodName;
    private double calorieAmount;
    private double proteinAmount;
    private double fatAmount;
    private double foodWeight;

    public static Order of(String foodName, double calorieAmount, double proteinAmount, double fatAmount, double foodWeight){
        return new Order(foodName, calorieAmount, proteinAmount, fatAmount, foodWeight);
    }

    public Order(String foodName, double calorieAmount, double proteinAmount, double fatAmount, double foodWeight) {
        this.foodName = foodName;
        this.calorieAmount = calorieAmount;
        this.proteinAmount = proteinAmount;
        this.fatAmount = fatAmount;
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

    public double getProteinAmount() {
        return proteinAmount;
    }

    public void setProteinAmount(double proteinAmount) {
        this.proteinAmount = proteinAmount;
    }

    public double getFatAmount() {
        return fatAmount;
    }

    public void setFatAmount(double fatAmount) {
        this.fatAmount = fatAmount;
    }
}
