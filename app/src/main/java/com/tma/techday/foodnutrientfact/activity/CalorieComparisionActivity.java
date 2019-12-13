package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.gui.dialog.NutritionComponentStatisticsDialog;
import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.model.NutritionStatisticsDTO;
import com.tma.techday.foodnutrientfact.service.CalorieDailyService;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Display pie chart and related parameters
 */
public class CalorieComparisionActivity extends AppCompatActivity {
    TextView calSettingView,pieChartTitle,excessCalories,excessCaloriesLabel,excessCaloriesLabelUnit;
    Button detailBtn;
    Spinner numberOfDateView;
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
     * Declare Params, Pie Chart and set text for view
     */
    public void setUpParam(){
        excessCalories = findViewById(R.id.txtcalRedundancy);
        excessCalories.setVisibility(View.INVISIBLE);
        excessCaloriesLabel = findViewById(R.id.calRedundancy);
        excessCaloriesLabel.setVisibility(View.INVISIBLE);
        excessCaloriesLabelUnit = findViewById(R.id.excessCaloriesLabelUnit);
        excessCaloriesLabelUnit.setVisibility(View.INVISIBLE);
        pieChartCalorie = findViewById(R.id.pieChartCal);
        pieChartTitle = findViewById(R.id.pieChartTitle);
        numberOfDateView = findViewById(R.id.spinnerNumberOfDay);
        detailBtn = findViewById(R.id.detailBtn);
        calSettingView = findViewById(R.id.txtCalSetting);
        CalorieDaily calorieDailyReview = (CalorieDaily) getIntent().getSerializableExtra(getString(R.string.calorie_daily_intent));
        pieChartTitle.setText(R.string.pie_chart_title);
        final String[] nbOfDate = new String[1];
        nbOfDate[0] = "1 Day";
        Date currentDate = new Date();
        calorieDailyList = calorieDailyService.getCalorieDaily(currentDate);

        double totalCalDaily = 0.0f;
        double totalProteinDaily = 0.0f;
        double totalFatDaily = 0.0f;
        if (calorieDailyReview != null) {
            totalCalDaily = calorieDailyReview.getCalorieDailyAmount();
            totalProteinDaily = calorieDailyReview.getProteinDailyAmount();
            totalFatDaily = calorieDailyReview.getFatDailyAmount();
        }

        for (CalorieDaily calorieDaily: calorieDailyList){
            totalCalDaily+= calorieDaily.getCalorieDailyAmount();
            totalProteinDaily += calorieDaily.getProteinDailyAmount();
            totalFatDaily += calorieDaily.getFatDailyAmount();
        }

        double calorieSetting = 0.0f;
        double proteinNecessAmount = 0.0f;
        double fatNecessAmount = 0.0f;
        if ( calorieSettingService.getCalorieSetting()!= null ) {
            calorieSetting = calorieSettingService.getCalorieSetting().getCalorieSettingAmount();
            proteinNecessAmount = calorieSettingService.getCalorieSetting().getProteinNecessAmount();
            fatNecessAmount = calorieSettingService.getCalorieSetting().getFatNecessAmount();
        }

        calSettingView.setText(numberFormat.format(calorieSetting));
        if (Float.compare((float)totalCalDaily,(float)calorieSetting) >= 0&&Float.compare((float)totalProteinDaily,(float)proteinNecessAmount) >= 0&&
                Float.compare((float)totalFatDaily,(float)fatNecessAmount) >= 0){
            detailBtn.setBackgroundColor(Color.GREEN);
        }else {
            detailBtn.setBackgroundColor(Color.YELLOW);
        }

        if (Float.compare((float) calorieSetting,0.0f) == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.error_title));
            builder.setMessage(getString(R.string.require_enter_calorie_setting));
            builder.show();
        }
        final double calorieSettingTmp = calorieSetting;
        final double totalCalDailyImp = totalCalDaily;
        numberOfDateView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nbOfDate[0] = parent.getItemAtPosition(position).toString();
                switch (nbOfDate[0]){
                    case "1 Day":
                        calSettingView.setText(numberFormat.format(calorieSettingTmp*1));
                        pieChartCalorie.clear();
                        if (pieDataSet!=null){
                            pieDataSet.clear();
                        }
                        try {
                            constructPieChart(totalCalDailyImp,numberFormat.parse(calSettingView.getText().toString()).doubleValue());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "3 Day":
                        calSettingView.setText(numberFormat.format(calorieSettingTmp * 3));
                        pieChartCalorie.clear();
                        pieDataSet.clear();
                        try {
                            constructPieChart(totalCalDailyImp,numberFormat.parse(calSettingView.getText().toString()).doubleValue());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "7 Day":
                        calSettingView.setText(numberFormat.format(calorieSettingTmp * 7));
                        pieChartCalorie.clear();
                        pieDataSet.clear();
                        try {
                            constructPieChart(totalCalDailyImp,numberFormat.parse(calSettingView.getText().toString()).doubleValue());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Click Button Detail event
        double finalProteinNecessAmount = proteinNecessAmount;
        double finalTotalProteinDaily = totalProteinDaily;
        double finalTotalFatDaily = totalFatDaily;
        double finalFatNecessAmount = fatNecessAmount;
        detailBtn.setOnClickListener(v -> {
            try {
               Double calSetting = numberFormat.parse(calSettingView.getText().toString()).doubleValue();
                Double numberOfDate = calSetting/calorieSettingTmp;
                NutritionStatisticsDTO dto = NutritionStatisticsDTO.of(rounDouble(totalCalDailyImp),rounDouble(finalTotalProteinDaily), rounDouble(finalTotalFatDaily),
                        calSetting, rounDouble(finalProteinNecessAmount*numberOfDate), rounDouble(finalFatNecessAmount*numberOfDate));
                NutritionComponentStatisticsDialog dialog = new NutritionComponentStatisticsDialog();
                Bundle args = new Bundle();   //Use bundle to pass data for nutrition statistic dialog
                args.putSerializable(getString(R.string.statisticdto), dto);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(),"Statistic_dialog");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    //Round 2 digits
    private double rounDouble(Double aDouble){
        return (Math.round(aDouble * 10.0) / 10.0);
    }

    /**
     * Build pie chart
     * @param totalCalDaily UserActivity today colories.
     * @param calorieSetting calorieSetting from db
     */
    private void constructPieChart(double totalCalDaily,double calorieSetting) {

        pieDataSet = new PieDataSet(pieEntriesCalories, " ");
        getEntries(totalCalDaily, calorieSetting);
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
     * Set up properties for pie chart calorie
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
        float calDailyPercentRounded = (float) (Math.round(calDailyPercent * 10.0) / 10.0);
        if (Float.compare(calDailyPercentRounded, 100) == 0) {
            pieEntriesCalories.add(new PieEntry(100,getString(R.string.current)));
            pieDataSet.setColors(Color.rgb(102,255,102) );
        }else if (Float.compare(calDailyPercentRounded, 100) > 0){
            excessCalories.setVisibility(View.VISIBLE);
            excessCaloriesLabel.setVisibility(View.VISIBLE);
            excessCaloriesLabelUnit.setVisibility(View.VISIBLE);
            excessCalories.setText(numberFormat.format(calDailyPercentRounded-100));
            pieEntriesCalories.add(new PieEntry(100," "));
            pieDataSet.setColors(Color.rgb(102,255,102) );
        }
        else {
            if (Float.compare(calDailyPercentRounded,50) > 0){
                excessCalories.setVisibility(View.INVISIBLE);
                excessCaloriesLabel.setVisibility(View.INVISIBLE);
                excessCaloriesLabelUnit.setVisibility(View.INVISIBLE);
                pieDataSet.setColors(Color.rgb(102,255,102),Color.rgb(155, 238, 255));
            }
            else if (Float.compare(calDailyPercentRounded, 50) == 0){
                excessCalories.setVisibility(View.INVISIBLE);
                excessCaloriesLabel.setVisibility(View.INVISIBLE);
                excessCaloriesLabelUnit.setVisibility(View.INVISIBLE);
                pieDataSet.setColors(Color.rgb(255,255,102),Color.rgb(155, 238, 255));
            }else {
                excessCalories.setVisibility(View.INVISIBLE);
                excessCaloriesLabel.setVisibility(View.INVISIBLE);
                excessCaloriesLabelUnit.setVisibility(View.INVISIBLE);
                pieDataSet.setColors(Color.rgb(233,63,63),Color.rgb(155, 238, 255));
            }
            pieEntriesCalories.add(new PieEntry(calDailyPercentRounded,getString(R.string.current)));
            pieEntriesCalories.add(new PieEntry(100 - calDailyPercentRounded,getString(R.string.calorie_missing)));
        }
    }
}
