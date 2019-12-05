package com.tma.techday.foodnutrientfact.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Contain and save Calorie Setting from DB
 */
public class CalorieSetting implements Serializable {
    private Date date;
    private double calorieSettingAmount;
    private double proteinNecessAmount;
    private double fatNecessAmount;


    public static CalorieSetting of(Date date, double calorieSettingAmount,double proteinNecessAmount, double fatNecessAmount){
        return new CalorieSetting(date, calorieSettingAmount,proteinNecessAmount,fatNecessAmount);
    }

    public CalorieSetting(Date date, double calorieSettingAmount, double proteinNecessAmount, double fatNecessAmount) {
        this.date = date;
        this.calorieSettingAmount = calorieSettingAmount;
        this.proteinNecessAmount = proteinNecessAmount;
        this.fatNecessAmount = fatNecessAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCalorieSettingAmount() {
        return calorieSettingAmount;
    }

    public void setCalorieSettingAmount(double calorieSettingAmount) {
        this.calorieSettingAmount = calorieSettingAmount;
    }

    public double getProteinNecessAmount() {
        return proteinNecessAmount;
    }

    public void setProteinNecessAmount(double proteinNecessAmount) {
        this.proteinNecessAmount = proteinNecessAmount;
    }

    public double getFatNecessAmount() {
        return fatNecessAmount;
    }

    public void setFatNecessAmount(double fatNecessAmount) {
        this.fatNecessAmount = fatNecessAmount;
    }
}
