package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    CardView cardViewDetect, cardViewCalSetting;


    /**
     * Switch from home screen to calorie setting and detect components
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);

        cardViewDetect.setOnClickListener(view -> {
            doOpenOtherActivity(DetectActivity.class);
        });

        cardViewCalSetting.setOnClickListener(view -> {
            doOpenOtherActivity(SettingActivity.class);
        });


    }

    /**
     * Switch to other activity
     * @param aClass destination activity
     */
    public void doOpenOtherActivity(Class aClass)
    {
        Intent intent =new Intent(this, aClass);
        startActivity(intent);
    }

    /**
     * Declare Params
     */
    public void setupParam(){
        cardViewDetect = findViewById(R.id.cardViewDetect);
        cardViewCalSetting = findViewById(R.id.cardViewCalSetting);
    }



}
