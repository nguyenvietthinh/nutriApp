package com.tma.techday.foodnutrientfact.gui.event;

public class CaloriesChangeEvent {

    public CaloriesChangeEvent(double caloriesChange, double proteinChange, double fatChange) {
        this.caloriesChange = caloriesChange;
        this.proteinChange = proteinChange;
        this.fatChange = fatChange;
    }

    public double getCaloriesChange() {
        return caloriesChange;
    }

    public double getProteinChange() {
        return proteinChange;
    }

    public double getFatChange() {
        return fatChange;
    }

    private double caloriesChange;
    private double proteinChange;
    private double fatChange;

}
