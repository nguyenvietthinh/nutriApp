package com.thinh.foodnutrientfact.model;

import java.io.Serializable;

/**
 * Fat information only used for display on UI
 */
public class FatInfo implements Serializable {

    private FatType fatType;
    private double amount;

    public FatInfo(FatType fatType, double amount) {
        this.fatType = fatType;
        this.amount = amount;
    }

    public FatType getFatType() {
        return fatType;
    }

    public void setFatType(FatType fatType) {
        this.fatType = fatType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
