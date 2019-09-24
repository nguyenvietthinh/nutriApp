package com.tma.techday.foodnutrientfact.gui.event;

public class CaloriesChangeEvent {

    public CaloriesChangeEvent(double caloriesChange) {
        this.caloriesChange = caloriesChange;
    }

    public double getCaloriesChange() {
        return caloriesChange;
    }

    private double caloriesChange;

}
