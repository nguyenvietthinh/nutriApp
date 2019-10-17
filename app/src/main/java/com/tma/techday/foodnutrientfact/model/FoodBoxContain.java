package com.tma.techday.foodnutrientfact.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class FoodBoxContain {
    private Rect rect;
    private Bitmap bitmapImage;

    public static FoodBoxContain of(Rect rect, Bitmap bitmapImage){
        return new FoodBoxContain(rect, bitmapImage);
    }

    public FoodBoxContain(Rect rect, Bitmap bitmapImage) {
        this.rect = rect;
        this.bitmapImage = bitmapImage;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }
}
