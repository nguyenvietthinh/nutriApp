package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.tma.techday.foodnutrientfact.service.CalorieDailyService;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CalorieComparisionActivity extends AppCompatActivity {
    TextView dateView, calDailyView, calSettingView;
    NumberFormat numberFormat = new DecimalFormat("0.00");
    List<CalorieDaily> calorieDailyList = new ArrayList<>();
    PieChart pieChartCalorie;
    List< PieEntry > pieEntriesCalories = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    double totalCalDaily,calorieSetting;

    @Inject
    CalorieSettingService calorieSettingService;
    @Inject
    CalorieDailyService calorieDailyService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_comparision_layout);

        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        setUpParam();

    }

    /**
     * Declare Params and Pie Chart
     */
    public void setUpParam(){
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(currentDate);
        pieChartCalorie = findViewById(R.id.pieChartCal);
        dateView = findViewById(R.id.date);
        calDailyView = findViewById(R.id.txtCalDaily);
        calSettingView = findViewById(R.id.txtCalSetting);
        CalorieDaily calorieDailyReview = (CalorieDaily) getIntent().getSerializableExtra("CalorieDaily");
        calorieDailyList = calorieDailyService.getCalorieDaily(currentDate);
        if(calorieDailyReview != null){
            totalCalDaily = calorieDailyReview.getCalorieDailyAmount();
        }
        for(CalorieDaily calorieDaily: calorieDailyList){
            totalCalDaily+= calorieDaily.getCalorieDailyAmount();
        }
        dateView.setText(date);
        calDailyView.setText(numberFormat.format(totalCalDaily));
        if(calorieSettingService.getCalorieSetting()!=null) {
            calorieSetting = calorieSettingService.getCalorieSetting().getCalorieSettingAmount();
        }
        calSettingView.setText(numberFormat.format(calorieSetting));
        if(calorieSetting != 0.0){

            getEntries(totalCalDaily,calorieSetting);
            pieDataSet = new PieDataSet(pieEntriesCalories, " ");
            pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter(pieChartCalorie));
            pieChartCalorie.setUsePercentValues(true);
            pieChartCalorie.setData(pieData);
            pieChartCalorie.setCenterText("Calorie Comparision Chart");
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(13f);
            pieDataSet.setSliceSpace(5f);
            Description description = new Description();
            description.setText("Pie Chart Calorie");
            pieChartCalorie.setDescription(description);

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please Enter Your Calorie Setting.");
            builder.show();
//            Intent intent =new Intent(this, SettingActivity.class);
//            startActivity(intent);
        }



    }


    /**
     * Get Entries of Chart
     */
    private void getEntries(double totalCalDaily, double calorieSetting) {
        float calDailyPercent = (float) (totalCalDaily/calorieSetting*100);
        if(calDailyPercent >= 100){
            pieEntriesCalories.add(new PieEntry(100,"Calorie Daily"));
        }else{
            pieEntriesCalories.add(new PieEntry(calDailyPercent,"Calorie Daily"));
            pieEntriesCalories.add(new PieEntry(100 - calDailyPercent," Missing"));
        }


    }
}
