package com.thinh.foodnutrientfact.model;

public class VitaminInfo {

    public VitaminInfo(VitaminType vitaminType, double amount) {
        this.vitaminType = vitaminType;
        this.amount = amount;
    }

    public VitaminType getVitaminType() {
        return vitaminType;
    }

    public void setVitaminType(VitaminType vitaminType) {
        this.vitaminType = vitaminType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private VitaminType vitaminType;
    private double amount;
}
