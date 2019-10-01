package com.tma.techday.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.CalorieSetting;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class SettingActivity extends AppCompatActivity {


    EditText txtCalSetting;
    Button btnSave;


    @Inject
    CalorieSettingService calorieSettingService;

    /**
     * Save user's calorie needs to database
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_setting);
        setUpParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
    }

    /**
     * Declare Params
     */
    private void setUpParam(){
        btnSave = findViewById(R.id.btnSave);
        txtCalSetting = findViewById(R.id.txtCalSetting);
        btnSave.setOnClickListener(setBtnSaveOnclickListener());
    }

    /**
     * set onclick listener for button Save
     * @return
     */
    private View.OnClickListener setBtnSaveOnclickListener() {
        return view -> {
                String calAmount = txtCalSetting.getText().toString();
                Log.i("rawAmount", calAmount);
                if (TextUtils.isEmpty(calAmount)) {
                    Toast.makeText(SettingActivity.this,getString(R.string.required_empty_text), Toast.LENGTH_LONG).show();
                } else {
                    double parseCalories = Double.parseDouble(calAmount);
                    CalorieSetting calorieSetting = buildCalorieSetting(parseCalories);
                    if(calorieSetting!= null) {
                        boolean isInserted = calorieSettingService.insertCalorieSetting(calorieSetting);
                        if (isInserted) {
                            Toast.makeText(SettingActivity.this, getString(R.string.save_successfully), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SettingActivity.this, getString(R.string.save_fail), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SettingActivity.this, getString(R.string.exists_calorie_setting), Toast.LENGTH_LONG).show();
                    }
                }
            };
    }

    /**
     * Build Calorie Setting
     * @return
     */
    private CalorieSetting buildCalorieSetting(double calorieSettingAmount) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(currentDate);
        if(calorieSettingService.getCalorieSetting()!= null){
            Date dateAdded = calorieSettingService.getCalorieSetting().getDate();
            String dateAdd = sdf.format(dateAdded);
            if(date.equals(dateAdd)){
                return null;
            }
        }

        return new CalorieSetting(currentDate,calorieSettingAmount);
    }
}
