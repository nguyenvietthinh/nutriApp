package com.tma.techday.foodnutrientfact.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Contain and save Calorie Daily from DB
 */
public class CalorieDaily implements Serializable {
    private Date date;
    private double calorieDailyAmount;

    public static CalorieDaily of(Date date, double calorieDailyAmount){
        return new CalorieDaily(date,calorieDailyAmount);
    }

    public CalorieDaily(Date date, double calorieDailyAmount) {
        this.date = date;
        this.calorieDailyAmount = calorieDailyAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCalorieDailyAmount() {
        return calorieDailyAmount;
    }

    public void setCalorieDailyAmount(double calorieDailyAmount) {
        this.calorieDailyAmount = calorieDailyAmount;
    }
}
