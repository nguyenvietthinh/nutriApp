package com.thinh.foodnutrientfact.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;

import javax.inject.Inject;

public class FoodNutriService extends Activity {

    private FoodNutriRepository foodNutriRepository;
    TextView txtFoodName,calories, protein, cholesterol,totalFat;
    View view;

    @Inject
    public FoodNutriService(FoodNutriRepository foodNutriRepository) {
        this.foodNutriRepository = foodNutriRepository;
    }


    public FoodInfoDTO getFoodNutri(String foodName){

//        StringBuffer buffer = new StringBuffer();

        FoodInfoDTO foodNutri = foodNutriRepository.getFoodNutri(foodName);
        if(foodNutri==null){
            return null;
        }
        else{
            return foodNutri;
        }

//        buffer.append("Potassium_(mg): "+ cursor.getString(14)+"\n");
//        buffer.append("Sodium_(mg): "+ cursor.getString(15)+"\n");
//
//        buffer.append("Vit_C_(mg): "+ cursor.getString(20)+"\n");
//        buffer.append("Thiamin_(mg): "+ cursor.getString(21)+"\n");
//
//        buffer.append("Vit_B6_(mg): "+ cursor.getString(25)+"\n");
//        buffer.append("Vit_B12_(µg): "+ cursor.getString(30)+"\n");
//        buffer.append("Vit_A_RAE: "+ cursor.getString(32)+"\n");
//        buffer.append("Vit_D_IU: "+ cursor.getString(41)+"\n");
//        buffer.append("FA_Sat_(g): "+ cursor.getString(43)+"\n");
//        buffer.append("FA_Mono_(g): "+ cursor.getString(44)+"\n");
//        buffer.append("FA_Poly_(g): "+ cursor.getString(45)+"\n");
//        buffer.append("Cholestrl_(mg): "+ cursor.getString(46)+"\n");



    }
}
