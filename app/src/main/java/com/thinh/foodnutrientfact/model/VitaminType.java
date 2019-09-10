package com.thinh.foodnutrientfact.model;

public enum VitaminType {
    A("Vitamin A"),
    B6("Vitamin B6"),
    B12("Vitamin B12"),
    C("Vitamin C"),
    D("Vitamin D");

    VitaminType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    private String desc;
}
