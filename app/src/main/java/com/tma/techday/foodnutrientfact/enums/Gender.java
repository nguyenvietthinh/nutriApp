package com.tma.techday.foodnutrientfact.enums;

public enum Gender {
    Male("Male"),
    Female("Female");

    Gender(String description){
        desc = description;
    }
    private String desc;

    @Override
    public String toString() {
        return desc;
    }
}
