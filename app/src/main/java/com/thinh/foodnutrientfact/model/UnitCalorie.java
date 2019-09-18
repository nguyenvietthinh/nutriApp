package com.thinh.foodnutrientfact.model;

public enum UnitCalorie {
    Kg("Kg"),
    Gram("Gram");

    UnitCalorie(String desc) {
        this.desc = desc;
    }

    private String desc;

    @Override
    public String toString() {
        return desc;
    }
}
