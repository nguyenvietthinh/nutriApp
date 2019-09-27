package com.tma.techday.foodnutrientfact.gui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.model.FoodInfoDTO;
import com.tma.techday.foodnutrientfact.service.CalorieDailyService;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CalorieComparisionDialog extends DialogFragment {

    AlertDialog dialog ;
    View calComparisionView;
    TextView dateView, calDailyView, calSettingView;
    NumberFormat numberFormat = new DecimalFormat("0.00");
    PieChart pieChartCalorie;
    List<PieEntry> pieEntriesCalories = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    CalorieDaily calorieDaily;
    double totalCalDaily,calorieSetting;

    @Inject
    CalorieSettingService calorieSettingService;
    @Inject
    CalorieDailyService calorieDailyService;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FoodNutriApplication application = (FoodNutriApplication)getActivity().getApplication();
        application.getComponent().inject(this);
        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        calComparisionView = inflater.inflate(R.layout.calorie_comparision_layout, null);
        init();
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //set background color transparent
//        dialog.getWindow().setDimAmount(0);  // remove background dim

        dialog.setView(calComparisionView);
        return dialog;
    }

    /**
     * Initialize param
     */
    private void init() {
        calorieDaily = getDataFromActivity();
        if(calorieDaily != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(calorieDaily.getDate());
            totalCalDaily = calorieDaily.getCalorieDailyAmount();
            pieChartCalorie = calComparisionView.findViewById(R.id.pieChartCal);
            dateView = calComparisionView.findViewById(R.id.date);
            calDailyView = calComparisionView.findViewById(R.id.txtCalDaily);
            calSettingView = calComparisionView.findViewById(R.id.txtCalSetting);
            dateView.setText(date);
            calDailyView.setText(numberFormat.format(totalCalDaily));
            if (calorieSettingService.getCalorieSetting() != null) {
                calorieSetting = calorieSettingService.getCalorieSetting().getCalorieSettingAmount();
            }
            calSettingView.setText(numberFormat.format(calorieSetting));
            if (calorieSetting != 0) {

                getEntries(totalCalDaily, (calorieSetting - totalCalDaily));
                pieDataSet = new PieDataSet(pieEntriesCalories, " ");
                pieData = new PieData(pieDataSet);
                pieData.setValueFormatter(new PercentFormatter(pieChartCalorie));
                pieChartCalorie.setUsePercentValues(true);
                pieChartCalorie.setData(pieData);
                pieChartCalorie.setCenterText("Calorie Comparision Chart");
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieDataSet.setSliceSpace(2f);
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(5f);
                pieChartCalorie.setTransparentCircleRadius(18f);
                pieChartCalorie.setHoleRadius(18f);
                Description description = new Description();
                description.setText("Pie Chart Calorie");
                pieChartCalorie.setDescription(description);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("Not Found");
                builder.show();
            }
        }
    }

    /**
     * Get Entries of Chart
     */
    private void getEntries(double totalCalDaily, double calorieSetting) {
        float calDailyPercent = (float) (totalCalDaily/calorieSetting*100);
        pieEntriesCalories.add(new PieEntry(calDailyPercent,"Calorie Daily"));
        pieEntriesCalories.add(new PieEntry(100 - calDailyPercent,"Calorie Missing"));

    }

    /**
     * Get data passed from Add to cart Activity
     * @return foodNutri
     */
    private CalorieDaily getDataFromActivity(){
        Bundle args = getArguments();
        calorieDaily = (CalorieDaily) args.getSerializable("calDaily");
        return calorieDaily;
    }

}
