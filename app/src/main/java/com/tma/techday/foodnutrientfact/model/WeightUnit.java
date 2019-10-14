package com.tma.techday.foodnutrientfact.model;

/**
 * Kind of weight unit
 */
public enum WeightUnit {
    Kg("Kg"),
    Gram("Gram");

    WeightUnit(String desc) {
        this.desc = desc;
    }

    public static WeightUnit fromDescription(String desc){
        if(desc == null)
            throw new IllegalArgumentException("Desc cannot be null");

        switch (desc){
            case "Kg":
                return Kg;
            case "Gram":
                return Gram;
        }
        throw new IllegalArgumentException("Desc can only be 'Gram' or 'Kg'.");
    }

    @Override
    public String toString() {
        return desc;
    }

    private String desc;
}
