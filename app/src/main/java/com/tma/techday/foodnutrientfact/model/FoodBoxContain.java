package com.tma.techday.foodnutrientfact.model;

import android.graphics.Rect;

public class FoodBoxContain {
    private Rect rect;
    private Integer trackingId;

    public static FoodBoxContain of(Rect rect, Integer trackingId){
        return new FoodBoxContain(rect, trackingId);
    }

    public FoodBoxContain(Rect rect, Integer trackingId) {
        this.rect = rect;
        this.trackingId = trackingId;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Integer getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(Integer trackingId) {
        this.trackingId = trackingId;
    }
}
