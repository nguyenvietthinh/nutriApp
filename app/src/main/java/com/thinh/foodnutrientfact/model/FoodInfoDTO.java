package com.thinh.foodnutrientfact.model;

import java.io.Serializable;
import java.util.List;

/**
 * Food information
 */
public class FoodInfoDTO implements Serializable {

    public static FoodInfoDTO constructFoodWithBasicInfo(String foodName, double calories, double totalFat, double protein){
        return new FoodInfoDTO(foodName, calories, totalFat, protein);
    }

    public static FoodInfoDTO constructFoodWithDetailInfo(String foodName, double calories, double totalFat, double protein, List<FatInfo> fatInfoList, int cholesterol, int sodium, int potassium, List<VitaminInfo> vitaminInfoList){
        return new FoodInfoDTO(foodName, calories, totalFat, protein,fatInfoList, cholesterol, sodium, potassium,vitaminInfoList);
    }

    public FoodInfoDTO(String foodName, double calories, double totalFat, double protein) {
        this.foodName = foodName;
        this.calories = calories;
        this.totalFat = totalFat;
        this.protein = protein;
    }

    public FoodInfoDTO(String foodName, double calories, double totalFat, double protein, List<FatInfo> fatInfoList, int cholesterol, int sodium, int potassium, List<VitaminInfo> vitaminInfoList) {
        this.foodName = foodName;
        this.calories = calories;
        this.totalFat = totalFat;
        this.protein = protein;
        this.fatInfoList = fatInfoList;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.potassium = potassium;
        this.vitaminInfoList = vitaminInfoList;
    }

    private String foodName;
    private double calories;
    private double totalFat;
    private double protein;
    private List<FatInfo> fatInfoList;
    private int cholesterol;
    private int sodium;
    private int potassium;
    private List<VitaminInfo> vitaminInfoList;


    public List<VitaminInfo> getVitaminInfoList() {
        return vitaminInfoList;
    }

    public void setVitaminInfoList(List<VitaminInfo> vitaminInfoList) {
        this.vitaminInfoList = vitaminInfoList;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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

    public void setTotalFat(double totalFar) {
        this.totalFat = totalFar;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public List<FatInfo> getFatInfoList() {
        return fatInfoList;
    }

    public void setFatInfoList(List<FatInfo> fatInfoList) {
        this.fatInfoList = fatInfoList;
    }

    public int getSodium() {

        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getPotassium() {
        return potassium;
    }

    public void setPotassium(int potassium) {
        this.potassium = potassium;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }
}
