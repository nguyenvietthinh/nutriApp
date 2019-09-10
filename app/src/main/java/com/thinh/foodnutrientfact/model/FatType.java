package com.thinh.foodnutrientfact.model;

public enum FatType {
    Sat("Saturated fat"),
    Poly("Polyunsaturated fat"),
    Mono("Monounsaturated fat");

    FatType(String desc) {
        this.desc = desc;
    }

    private String desc;

    @Override
    public String toString() {
        return desc;
    }
}
