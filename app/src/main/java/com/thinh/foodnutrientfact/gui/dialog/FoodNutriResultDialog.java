package com.thinh.foodnutrientfact.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.andremion.counterfab.CounterFab;
import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.activity.AddToCartActivity;
import com.thinh.foodnutrientfact.activity.MainActivity;
import com.thinh.foodnutrientfact.activity.SettingActivity;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;
import com.thinh.foodnutrientfact.model.FatInfo;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.model.Order;
import com.thinh.foodnutrientfact.model.VitaminInfo;
import com.thinh.foodnutrientfact.service.FoodNutriService;
import com.thinh.foodnutrientfact.service.OrderService;

import java.util.List;

import javax.inject.Inject;

public class FoodNutriResultDialog extends DialogFragment {

    View viewResult,viewDetailRessult,viewDialog, popupInputDialogView;
    TextView DetailFoodNameView, FoodNameView, caloriesView, totalFatView, cholesterolView, proteinView, satFatView, polyFatView, monoFatView, sodiumView, potassiumView, vitCView, vitDView, vitAView,vitB6View,
            vitB12View, caloriesDetailView, proteinDetailView, cholesterolDetailView, totalFatDetailView,popupDialogFoodName;
    ImageButton btnCloseDialog;
    Button btnAdd, btnDetailAdd,btnpopupDialogCancel,btnpopupDialogAdd;
    AlertDialog dialog, popupDialog ;
    FoodInfoDTO foodNutri;
    Order order;
    CounterFab counterFab;
    EditText popupDialogFoodWeight;

    @Inject
    FoodNutriService foodNutriService;

    @Inject
    OrderService orderService;


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
        viewDialog = inflater.inflate(R.layout.alertdialog_nutri_result_layout, null);
        init();
        foodNutri = getDataFromActivity();
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
        dialog.setView(viewDialog);
        btnCloseDialog.setOnClickListener(view -> dialog.cancel());
        btnAdd.setOnClickListener(view -> {
            showPopupDialog();

        });
        btnDetailAdd.setOnClickListener(view -> {
            showPopupDialog();

        });

        btnpopupDialogCancel.setOnClickListener(view -> popupDialog.cancel());

        btnpopupDialogAdd.setOnClickListener(view -> {
            if(orderService.addOrderToCard(new Order(foodNutri.getFoodName(),foodNutri.getCalories(),100))){
                Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_LONG).show();
                viewDialog.setVisibility(View.GONE);
                counterFab.setCount(orderService.getCountCart());
            }else{
                Toast.makeText(getActivity(), "Can not add to cart", Toast.LENGTH_LONG).show();
                viewDialog.setVisibility(View.GONE);
            }
        });

        viewResult.setOnClickListener(view -> {
            viewDetailRessult.setVisibility(View.VISIBLE);
            viewResult.setVisibility(View.GONE);
        });
        viewDetailRessult.setOnClickListener(view -> {
            viewResult.setVisibility(View.VISIBLE);
            viewDetailRessult.setVisibility(View.GONE);
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //set background color transparent
        dialog.getWindow().setDimAmount(0);  // remove background dim
        dialog.setView(viewDialog);
        return dialog;
    }

    /**
     * Initialize param
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
        btnCloseDialog = viewDialog.findViewById(R.id.imageButton_close);
        btnAdd = viewDialog.findViewById(R.id.btnAdd);
        btnDetailAdd = viewDetailRessult.findViewById(R.id.btnAdd);
        counterFab = getActivity().findViewById(R.id.fab);
    }

    /**
     * Get data passed from Activity
     * @return foodNutri
     */
    private FoodInfoDTO getDataFromActivity(){
        Bundle args = getArguments();
        foodNutri = (FoodInfoDTO) args.getSerializable("foodNutri");
        return foodNutri;
    }

    /**
     * Switch to other activity
     * @param aClass destination activity
     */
    public void doOpenOtherActivity(Class aClass)
    {
        Intent intent =new Intent(getActivity(), aClass);
        startActivity(intent);
    }

    /**
     *Initialize popup dialog.
     */
    private void initPopupDialog()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();;

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_food_weight_dialog, null);

        setUpParamPopupDialog();

        // Display values from the main activity list view in user input edittext.
        initTextViewFoodNameInPopupDialog();
    }

    /**
     *  Get current food name and set them in the popup dialog text view.
     */
    private void initTextViewFoodNameInPopupDialog()
    {
        FoodInfoDTO foodInfoDTO = getDataFromActivity();
        popupDialogFoodName.setText(foodInfoDTO.getFoodName());

    }

    /**
     * Declare Params for Popup dialog
     */
    private void setUpParamPopupDialog(){

        popupDialogFoodName = popupInputDialogView.findViewById(R.id.popupFoodName);
        popupDialogFoodWeight = popupInputDialogView.findViewById(R.id.userName);
        btnpopupDialogAdd = popupInputDialogView.findViewById(R.id.btnAddToCart);
        btnpopupDialogCancel = popupInputDialogView.findViewById(R.id.btnCancel);
    }

    /**
     * Show popup dialog
     */
    private void showPopupDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Enter Food Weight.");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(false);
        initPopupDialog();
        alertDialogBuilder.setView(popupInputDialogView);
        popupDialog = alertDialogBuilder.create();
        popupDialog.show();
    }
}
