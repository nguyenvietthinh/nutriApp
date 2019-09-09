package com.thinh.foodnutrientfact.enums;

public enum ImageDetectEngine {
    DEVICE_ENGINE("Use internal device to analyze the image"),
    COULD_ENGINE("Use the firebase cloud to analyze the image");

    ImageDetectEngine(String description){
        desc = description;
    }
    private String desc;
}
