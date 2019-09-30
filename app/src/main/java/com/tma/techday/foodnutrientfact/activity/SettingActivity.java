package com.tma.techday.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.CalorieDaily;
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
        btnSave = findViewById(R.id.btnAdd);
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
                //TODO: log raw amount to file
                Log.i("rawAmount", calAmount);
                if (TextUtils.isEmpty(calAmount)) {
                    Toast.makeText(SettingActivity.this, "Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
                } else {
                    double parseCalories = Double.parseDouble(calAmount);
                    CalorieSetting calorieSetting = buildCalorieSetting(parseCalories);
                    if(calorieSetting!= null) {
                        boolean isInserted = calorieSettingService.insertCalorieSetting(calorieSetting);
                        if (isInserted) {
                            Toast.makeText(SettingActivity.this, "Save Data Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SettingActivity.this, "Save To Data Failed", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(SettingActivity.this, "Today you entered calorie", Toast.LENGTH_LONG).show();
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
