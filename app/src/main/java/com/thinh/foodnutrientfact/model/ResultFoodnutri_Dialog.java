package com.thinh.foodnutrientfact.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.service.FoodNutriService;

import javax.inject.Inject;

public class ResultFoodnutri_Dialog extends DialogFragment {

    View viewResult,viewDetailRessult,viewDialog;
    TextView txtDFoodNutri,txtFoodName,calories,totalFat,cholesterol,protein,satFat,polyFat,monoFat,sodium,potassium,vitC,vitD,vitA,vitB6,
            vitB12, caloriesDetail, proteinDetail, cholesterolDetail, totalFatDetail;
    ImageButton closeButton;
    AlertDialog builder;

    @Inject
    FoodNutriService foodNutriService;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        builder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewDialog = inflater.inflate(R.layout.alertdialog_nutri_result_layout, null);
        setParam();
        Bundle args = getArguments();

        FoodInfoDTO foodNutri = (FoodInfoDTO) args.getSerializable("foodNutri");
        txtFoodName.setText(foodNutri.getFoodName());
        calories.setText(Double.toString(foodNutri.getCalories()));
        protein.setText(Double.toString(foodNutri.getProtein()));
        cholesterol.setText(Integer.toString(foodNutri.getCholesterol()));
        totalFat.setText(Double.toString(foodNutri.getTotalFat()));
        txtDFoodNutri.setText(foodNutri.getFoodName());
        caloriesDetail.setText(Double.toString(foodNutri.getCalories()));
        proteinDetail.setText(Double.toString(foodNutri.getProtein()));
        cholesterolDetail.setText(Integer.toString(foodNutri.getCholesterol()));
        totalFatDetail.setText(Double.toString(foodNutri.getTotalFat()));
        for (FatInfo fatInfo : foodNutri.getFatInfoList()) {
            switch (fatInfo.getFatType()){
                case Sat:
                    satFat.setText(Double.toString(fatInfo.getAmount()));
                    break;
                case Poly:
                    polyFat.setText(Double.toString(fatInfo.getAmount()));
                    break;
                case Mono:
                    monoFat.setText(Double.toString(fatInfo.getAmount()));
                    break;
            }
        }
        sodium.setText(Integer.toString(foodNutri.getSodium()));
        potassium.setText(Integer.toString(foodNutri.getPotassium()));
        for (VitaminInfo vitaminInfo : foodNutri.getVitaminInfoList()) {
            switch (vitaminInfo.getVitaminType()){
                case C:
                    vitC.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case A:
                    vitA.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case D:
                    vitD.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case B6:
                    vitB6.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case B12:
                    vitB12.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
            }
        }


        builder.setView(viewDialog);
        closeButton = viewDialog.findViewById(R.id.imageButton_close);
        closeButton.setOnClickListener(view -> builder.cancel());
        viewResult.setOnClickListener(view -> {
            viewDetailRessult.setVisibility(View.VISIBLE);
            viewResult.setVisibility(View.INVISIBLE);
        });
        viewDetailRessult.setOnClickListener(view -> {
            viewResult.setVisibility(View.VISIBLE);
            viewDetailRessult.setVisibility(View.GONE);
        });
        viewDialog.setBackgroundColor(Color.TRANSPARENT);
        viewResult.setBackgroundColor(Color.TRANSPARENT);
        viewDetailRessult.setBackgroundColor(Color.TRANSPARENT);
        builder.setView(viewDialog);
        return builder;
    }

    /**
     *
     */
    public void setParam(){
        viewResult = viewDialog.findViewById(R.id.resultLayout);
        viewDetailRessult = viewDialog.findViewById(R.id.resultDetailLayout);
        viewDetailRessult.setVisibility(View.GONE);

        txtFoodName = viewDialog.findViewById(R.id.txtFoodName);
        calories = viewDialog.findViewById(R.id.txtCal);
        protein = viewDialog.findViewById(R.id.txtProtein);
        cholesterol = viewDialog.findViewById(R.id.txtCholesterol);
        totalFat = viewDialog.findViewById(R.id.txtTotalFat);
        satFat = viewDialog.findViewById(R.id.txtSaturatedFat);
        polyFat = viewDialog.findViewById(R.id.txtPolyunsaturatedFat);
        monoFat = viewDialog.findViewById(R.id.txtMonounsaturatedFat);
        sodium = viewDialog.findViewById(R.id.txtSodium);
        potassium = viewDialog.findViewById(R.id.txtPotassium);
        vitA = viewDialog.findViewById(R.id.txtVitaminA);
        vitC = viewDialog.findViewById(R.id.txtVitaminC);
        vitD = viewDialog.findViewById(R.id.txtVitaminD);
        vitB6 = viewDialog.findViewById(R.id.txtVitaminB6);
        vitB12 = viewDialog.findViewById(R.id.txtVitaminB12);

        txtDFoodNutri = viewDetailRessult.findViewById(R.id.txtFoodName);
        caloriesDetail = viewDetailRessult.findViewById(R.id.txtCal);
        proteinDetail = viewDetailRessult.findViewById(R.id.txtProtein);
        cholesterolDetail = viewDetailRessult.findViewById(R.id.txtCholesterol);
        totalFatDetail = viewDetailRessult.findViewById(R.id.txtTotalFat);
    }

}
