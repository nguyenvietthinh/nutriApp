package com.tma.techday.foodnutrientfact.model;

import java.io.Serializable;
import java.util.Date;

public class CalorieSetting implements Serializable {
    private Date date;
    private double calorieSettingAmount;

    public static CalorieSetting of(Date date, double calorieSettingAmount){
        return new CalorieSetting(date, calorieSettingAmount);
    }

    public CalorieSetting(Date date, double calorieSettingAmount) {
        this.date = date;
        this.calorieSettingAmount = calorieSettingAmount;
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
}
