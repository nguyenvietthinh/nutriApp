package com.tma.techday.foodnutrientfact.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalorieDaily {
    private String date;
    private double calorieAmountDaily;

    public CalorieDaily(String date, double calorieAmountDaily) {
        this.date = date;
        this.calorieAmountDaily = calorieAmountDaily;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = date;

    }

    public double getCalorieAmountDaily() {
        return calorieAmountDaily;
    }

    public void setCalorieAmountDaily(double calorieAmountDaily) {
        this.calorieAmountDaily = calorieAmountDaily;
    }
}
