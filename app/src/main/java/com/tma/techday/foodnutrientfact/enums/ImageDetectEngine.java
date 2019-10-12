package com.tma.techday.foodnutrientfact.enums;

public enum ImageDetectEngine {
    DEVICE_ENGINE("Use internal device to analyze the image"),
    CLOUD_ENGINE("Use the firebase cloud to analyze the image");

    ImageDetectEngine(String description){
        desc = description;
    }
    private String desc;
}
