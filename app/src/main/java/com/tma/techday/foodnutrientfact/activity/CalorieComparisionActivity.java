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
import com.tma.techday.foodnutrientfact.helper.SystemConstant;
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
        CalorieDaily calorieDailyReview = (CalorieDaily) getIntent().getSerializableExtra(getString(R.string.calorie_daily_intent));
        pieChartTitle.setText(R.string.pie_chart_title);

        Date currentDate = new Date();
        calorieDailyList = calorieDailyService.getCalorieDaily(currentDate);

        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
        String date = sdf.format(currentDate);
        dateView.setText(date);

        double totalCalDaily = 0.0f;
        if (calorieDailyReview != null) {
            totalCalDaily = calorieDailyReview.getCalorieDailyAmount();
        }

        for (CalorieDaily calorieDaily: calorieDailyList){
            totalCalDaily+= calorieDaily.getCalorieDailyAmount();
        }
        calDailyView.setText(numberFormat.format(totalCalDaily));

        double calorieSetting = 0.0f;
        if ( calorieSettingService.getCalorieSetting()!= null ) {
            calorieSetting = calorieSettingService.getCalorieSetting().getCalorieSettingAmount();
        }
        calSettingView.setText(numberFormat.format(calorieSetting));
        if (Float.compare((float) calorieSetting,0.0f) != 0) {
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
        pieData.setValueTextColor(Color.BLACK);

        pieChartCalorie.setData(pieData);

        setupPieDataSet(pieDataSet);

        setupPieChartCalorie(pieChartCalorie);

        Description description = new Description();
        description.setText(getString(R.string.pie_chart_des));
        pieChartCalorie.setDescription(description);
    }

    /**
     * ...
     * @param pieChartCalorie pieChartCalorie
     */
    private static void setupPieChartCalorie(PieChart pieChartCalorie) {
        pieChartCalorie.setUsePercentValues(true);
        pieChartCalorie.setDrawHoleEnabled(true);
        pieChartCalorie.setTransparentCircleRadius(0f);
        pieChartCalorie.setHoleRadius(0f);
        pieChartCalorie.setEntryLabelColor(Color.BLACK);
    }

    /**
     * Set up: colors, text size, x y....
     * @param pieDataSet pieDataSet
     */
    private static void setupPieDataSet(PieDataSet pieDataSet) {
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setValueTextSize(13f);
        pieDataSet.setSliceSpace(5f);
        pieDataSet.setValueLinePart1OffsetPercentage(90.f);
        pieDataSet.setValueLinePart1Length(1.2f);
        pieDataSet.setValueLinePart2Length(.4f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    }

    /**
     * Get Entries of Chart
     */
    private void getEntries(double totalCalDaily, double calorieSetting) {
        float calDailyPercent = (float) (totalCalDaily/calorieSetting*100);
        if (Float.compare(calDailyPercent,100) >= 0) {
            pieEntriesCalories.add(new PieEntry(100," "));
        } else {
            pieEntriesCalories.add(new PieEntry(calDailyPercent,getString(R.string.current)));
            pieEntriesCalories.add(new PieEntry(100 - calDailyPercent,getString(R.string.calorie_missing)));
        }
    }
}
