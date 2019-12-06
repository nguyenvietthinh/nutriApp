package com.tma.techday.foodnutrientfact.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.NutritionStatisticsDTO;

public class NutritionComponentStatisticsDialog extends DialogFragment {

    AlertDialog dialog;
    View viewDialog;
    TextView calSetting, actualCalorie, proteinNecess, actualProtein, totalFatNecess, actualtotalFat;
    ImageButton btnCloseDialog;
    NutritionStatisticsDTO nutritionStatisticsDTO;

    /**
     * Create Alert Dialog Fragment For Result Food Nutrition
     * @param savedInstanceState
     * @return alert dialog result food nutrition for main activity
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FoodNutriApplication application = (FoodNutriApplication)getActivity().getApplication();
        application.getComponent().inject(this);
        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewDialog = inflater.inflate(R.layout.nutrition_component_statistics_dialog, null);
        init();
        setTextForTextView();
        setUpDialog();
        setClickListener();
        return dialog;
    }

    /**
     * Set up properties for dialog
     */
    private void setUpDialog() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //set background color transparent
        dialog.getWindow().setDimAmount(0);  // remove background dim
        dialog.setCancelable(true);
        dialog.setView(viewDialog);
    }

    /**
     * Set text for text View
     */
    private void setTextForTextView() {
        nutritionStatisticsDTO = getDataFromActivity();
        calSetting.setText(Double.toString(nutritionStatisticsDTO.getCaloriesSetting()));
        actualCalorie.setText(Double.toString(nutritionStatisticsDTO.getCalories()));
        proteinNecess.setText(Double.toString(nutritionStatisticsDTO.getProteinNecess()));
        actualProtein.setText(Double.toString(nutritionStatisticsDTO.getProtein()));
        totalFatNecess.setText(Double.toString(nutritionStatisticsDTO.getTotalFatNecess()));
        actualtotalFat.setText(Double.toString(nutritionStatisticsDTO.getTotalFat()));
    }

    /**
     * Get data passed from Activity
     * @return foodNutri
     */
    private NutritionStatisticsDTO getDataFromActivity(){
        Bundle args = getArguments();
        nutritionStatisticsDTO = (NutritionStatisticsDTO) args.getSerializable(getString(R.string.statisticdto));
        return nutritionStatisticsDTO;
    }

    /**
     * Initialize param
     */
    public void init(){
        calSetting = viewDialog.findViewById(R.id.txtCalSetting);
        actualCalorie = viewDialog.findViewById(R.id.txtCal);
        proteinNecess = viewDialog.findViewById(R.id.txtProteinNecess);
        actualProtein = viewDialog.findViewById(R.id.txtProtein);
        totalFatNecess = viewDialog.findViewById(R.id.txtTotalFatNecess);
        actualtotalFat = viewDialog.findViewById(R.id.txtTotalFat);
        btnCloseDialog = viewDialog.findViewById(R.id.imageButton_close);
    }

    /**
     * Set click listener for view and button
     */
    private void setClickListener() {
        btnCloseDialog.setOnClickListener(view -> dialog.cancel());
    }
}
