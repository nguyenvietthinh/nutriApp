package com.tma.techday.foodnutrientfact.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Contain and save Calorie Daily from DB
 */
public class CalorieDaily implements Serializable {
    private Date date;
    private double calorieDailyAmount;
    private double proteinDailyAmount;
    private double fatDailyAmount;

    public static CalorieDaily of(Date date, double calorieDailyAmount,double proteinDailyAmount, double fatDailyAmount){
        return new CalorieDaily(date,calorieDailyAmount,proteinDailyAmount,fatDailyAmount);
    }

    public CalorieDaily(Date date, double calorieDailyAmount, double proteinDailyAmount, double fatDailyAmount) {
        this.date = date;
        this.calorieDailyAmount = calorieDailyAmount;
        this.proteinDailyAmount = proteinDailyAmount;
        this.fatDailyAmount = fatDailyAmount;
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

    public double getProteinDailyAmount() {
        return proteinDailyAmount;
    }

    public void setProteinDailyAmount(double proteinDailyAmount) {
        this.proteinDailyAmount = proteinDailyAmount;

    }

    public double getFatDailyAmount() {
        return fatDailyAmount;
    }

    public void setFatDailyAmount(double fatDailyAmount) {
        this.fatDailyAmount = fatDailyAmount;
    }
}
