package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
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
    TextView dateView, calDailyView, calSettingView,pieChartTitle;
    NumberFormat numberFormat = new DecimalFormat("0.00");
    List<CalorieDaily> calorieDailyList = new ArrayList<>();
    PieChart pieChartCalorie;
    List< PieEntry > pieEntriesCalories = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;

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
        pieChartCalorie = findViewById(R.id.pieChartCal);
        pieChartTitle = findViewById(R.id.pieChartTitle);
        dateView = findViewById(R.id.date);
        calDailyView = findViewById(R.id.txtCalDaily);
        calSettingView = findViewById(R.id.txtCalSetting);
        CalorieDaily calorieDailyReview = (CalorieDaily) getIntent().getSerializableExtra("CalorieDaily");

        Date currentDate = new Date();
        calorieDailyList = calorieDailyService.getCalorieDaily(currentDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(currentDate);
        dateView.setText(date);

        double totalCalDaily = 0.0f ;
        if(calorieDailyReview != null){
            totalCalDaily = calorieDailyReview.getCalorieDailyAmount();
        }
        for(CalorieDaily calorieDaily: calorieDailyList){
            totalCalDaily+= calorieDaily.getCalorieDailyAmount();
        }
        calDailyView.setText(numberFormat.format(totalCalDaily));

        double calorieSetting = 0.0f;
        if( calorieSettingService.getCalorieSetting()!= null ) {
            calorieSetting = calorieSettingService.getCalorieSetting().getCalorieSettingAmount();
        }
        calSettingView.setText(numberFormat.format(calorieSetting));
        if (calorieSetting != 0.0) {
            constructPieChart(totalCalDaily,calorieSetting);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.error_title));
            builder.setMessage(getString(R.string.require_enter_calorie_setting));
            builder.show();
        }
    }

    /**
     * Build pie chart
     * @param totalCalDaily user today colories.
     * @param calorieSetting calorieSetting from db
     */
    private void constructPieChart(double totalCalDaily,double calorieSetting) {
        getEntries(totalCalDaily, calorieSetting);
        pieDataSet = new PieDataSet(pieEntriesCalories, " ");
        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChartCalorie));
        pieChartCalorie.setUsePercentValues(true);
        pieChartCalorie.setData(pieData);
        pieChartCalorie.setCenterText(getString(R.string.pie_chart_title));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setSliceSpace(5f);
        Description description = new Description();
        description.setText(getString(R.string.pie_chart_des));
        pieChartCalorie.setDescription(description);
    }


    /**
     * Get Entries of Chart
     */
    private void getEntries(double totalCalDaily, double calorieSetting) {
        float calDailyPercent = (float) (totalCalDaily/calorieSetting*100);
        if(Float.compare(calDailyPercent,100) >= 0) {
            pieEntriesCalories.add(new PieEntry(100,getString(R.string.calorie_daily)));
        } else {
            pieEntriesCalories.add(new PieEntry(calDailyPercent,getString(R.string.calorie_daily)));
            pieEntriesCalories.add(new PieEntry(100 - calDailyPercent,getString(R.string.calorie_missing)));
        }
    }
}
