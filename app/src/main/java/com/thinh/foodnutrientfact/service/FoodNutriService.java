package com.thinh.foodnutrientfact.service;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;

import javax.inject.Inject;

public class FoodNutriService {

    private FoodNutriRepository foodNutriRepository;

    @Inject
    public FoodNutriService(FoodNutriRepository foodNutriRepository) {
        this.foodNutriRepository = foodNutriRepository;
    }

    public String getFoodNutri(String foodName){

        FoodInfoDTO foodNutri = foodNutriRepository.getFoodNutri(foodName);
        StringBuffer buffer = new StringBuffer();
        if(foodNutri==null){
            return null;
        }
        else{

            buffer.append("Food Name: "+ foodName +"\n");
            buffer.append("Calories: "+ foodNutri.getCalories()+"\n");
            buffer.append("Protein: "+ foodNutri.getProtein()+"\n");
            buffer.append("Cholesterol: "+ foodNutri.getCholesterol()+"\n");
            buffer.append("Total Fat: "+ foodNutri.getTotalFat()+"\n");;

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


        return buffer.toString();
    }
}
