package com.tma.techday.foodnutrientfact.model;

import java.io.Serializable;

public class NutritionStatisticsDTO implements Serializable {
    private double calories;
    private double totalFat;
    private double protein;
    private double caloriesSetting;
    private double totalFatNecess;
    private double proteinNecess;

    public static NutritionStatisticsDTO of(double calories,double protein, double totalFat, double caloriesSetting, double proteinNecess, double totalFatNecess){
        return new NutritionStatisticsDTO(calories,protein,totalFat,caloriesSetting,proteinNecess,totalFatNecess);
    }

    public NutritionStatisticsDTO(double calories,double protein, double totalFat,  double caloriesSetting,double proteinNecess, double totalFatNecess) {
        this.calories = calories;
        this.totalFat = totalFat;
        this.protein = protein;
        this.caloriesSetting = caloriesSetting;
        this.totalFatNecess = totalFatNecess;
        this.proteinNecess = proteinNecess;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCaloriesSetting() {
        return caloriesSetting;
    }

    public void setCaloriesSetting(double caloriesSetting) {
        this.caloriesSetting = caloriesSetting;
    }

    public double getTotalFatNecess() {
        return totalFatNecess;
    }

    public void setTotalFatNecess(double totalFatNecess) {
        this.totalFatNecess = totalFatNecess;
    }

    public double getProteinNecess() {
        return proteinNecess;
    }

    public void setProteinNecess(double proteinNecess) {
        this.proteinNecess = proteinNecess;
    }
}
