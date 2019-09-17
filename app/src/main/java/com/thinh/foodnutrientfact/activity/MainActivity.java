package com.thinh.foodnutrientfact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;


public class MainActivity extends AppCompatActivity {

    Button btnDetect, btnSetting;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        btnDetect.setOnClickListener(view -> {
            doOpenOtherActivity(DetectActivity.class);
        });

        btnSetting.setOnClickListener(view -> {
            doOpenOtherActivity(SettingActivity.class);
        });


    }

    public void doOpenOtherActivity(Class aClass)
    {
        Intent intent =new Intent(this, aClass);
        startActivity(intent);
    }
    public void setupParam(){
        btnDetect = findViewById(R.id.btnDetect);
        btnSetting = findViewById(R.id.btnSetting);
    }
}
