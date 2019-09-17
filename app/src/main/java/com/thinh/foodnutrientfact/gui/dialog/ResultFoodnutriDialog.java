package com.thinh.foodnutrientfact.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.model.FatInfo;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.model.VitaminInfo;
import com.thinh.foodnutrientfact.service.FoodNutriService;

import javax.inject.Inject;

public class ResultFoodnutriDialog extends DialogFragment {

    View viewResult,viewDetailRessult,viewDialog;
    TextView DetailFoodNameView, FoodNameView, caloriesView, totalFatView, cholesterolView, proteinView, satFatView, polyFatView, monoFatView, sodiumView, potassiumView, vitCView, vitDView, vitAView,vitB6View,
            vitB12View, caloriesDetailView, proteinDetailView, cholesterolDetailView, totalFatDetailView;
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
        init();
        Bundle args = getArguments();
        FoodInfoDTO foodNutri = (FoodInfoDTO) args.getSerializable("foodNutri");
        FoodNameView.setText(foodNutri.getFoodName());
        caloriesView.setText(Double.toString(foodNutri.getCalories()));
        proteinView.setText(Double.toString(foodNutri.getProtein()));
        cholesterolView.setText(Integer.toString(foodNutri.getCholesterol()));
        totalFatView.setText(Double.toString(foodNutri.getTotalFat()));
        DetailFoodNameView.setText(foodNutri.getFoodName());
        caloriesDetailView.setText(Double.toString(foodNutri.getCalories()));
        proteinDetailView.setText(Double.toString(foodNutri.getProtein()));
        cholesterolDetailView.setText(Integer.toString(foodNutri.getCholesterol()));
        totalFatDetailView.setText(Double.toString(foodNutri.getTotalFat()));
        for (FatInfo fatInfo : foodNutri.getFatInfoList()) {
            switch (fatInfo.getFatType()){
                case Sat:
                    satFatView.setText(Double.toString(fatInfo.getAmount()));
                    break;
                case Poly:
                    polyFatView.setText(Double.toString(fatInfo.getAmount()));
                    break;
                case Mono:
                    monoFatView.setText(Double.toString(fatInfo.getAmount()));
                    break;
            }
        }
        sodiumView.setText(Integer.toString(foodNutri.getSodium()));
        potassiumView.setText(Integer.toString(foodNutri.getPotassium()));
        for (VitaminInfo vitaminInfo : foodNutri.getVitaminInfoList()) {
            switch (vitaminInfo.getVitaminType()){
                case C:
                    vitCView.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case A:
                    vitAView.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case D:
                    vitDView.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case B6:
                    vitB6View.setText(Double.toString(vitaminInfo.getAmount()));
                    break;
                case B12:
                    vitB12View.setText(Double.toString(vitaminInfo.getAmount()));
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
    public void init(){
        viewResult = viewDialog.findViewById(R.id.resultLayout);
        viewDetailRessult = viewDialog.findViewById(R.id.resultDetailLayout);
        viewDetailRessult.setVisibility(View.GONE);
        FoodNameView = viewDialog.findViewById(R.id.txtFoodName);
        caloriesView = viewDialog.findViewById(R.id.txtCal);
        proteinView = viewDialog.findViewById(R.id.txtProtein);
        cholesterolView = viewDialog.findViewById(R.id.txtCholesterol);
        totalFatView = viewDialog.findViewById(R.id.txtTotalFat);
        satFatView = viewDialog.findViewById(R.id.txtSaturatedFat);
        polyFatView = viewDialog.findViewById(R.id.txtPolyunsaturatedFat);
        monoFatView = viewDialog.findViewById(R.id.txtMonounsaturatedFat);
        sodiumView = viewDialog.findViewById(R.id.txtSodium);
        potassiumView = viewDialog.findViewById(R.id.txtPotassium);
        vitAView = viewDialog.findViewById(R.id.txtVitaminA);
        vitCView = viewDialog.findViewById(R.id.txtVitaminC);
        vitDView = viewDialog.findViewById(R.id.txtVitaminD);
        vitB6View = viewDialog.findViewById(R.id.txtVitaminB6);
        vitB12View = viewDialog.findViewById(R.id.txtVitaminB12);
        DetailFoodNameView = viewDetailRessult.findViewById(R.id.txtFoodName);
        caloriesDetailView = viewDetailRessult.findViewById(R.id.txtCal);
        proteinDetailView = viewDetailRessult.findViewById(R.id.txtProtein);
        cholesterolDetailView = viewDetailRessult.findViewById(R.id.txtCholesterol);
        totalFatDetailView = viewDetailRessult.findViewById(R.id.txtTotalFat);
    }

}
