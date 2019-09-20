package com.thinh.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;
import com.thinh.foodnutrientfact.service.CalorieSettingService;

import javax.inject.Inject;

public class SettingActivity extends AppCompatActivity {


    EditText txtCalSetting;
    Button btnSave;
    Spinner spinner;

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
        spinner =  findViewById(R.id.planets_spinner);
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
                    boolean isInserted = calorieSettingService.insertCalorieSetting(parseCalories);
                    if (isInserted) {
                        Toast.makeText(SettingActivity.this, "Save Data Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "Save To Data Failed", Toast.LENGTH_LONG).show();
                    }

                }
            };
    }
}
