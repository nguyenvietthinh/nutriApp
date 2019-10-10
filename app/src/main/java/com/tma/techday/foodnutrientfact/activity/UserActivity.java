package com.tma.techday.foodnutrientfact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.UserService;

import javax.inject.Inject;

public class UserActivity extends AppCompatActivity {

    Button btnEdit;
    TextView txtUserName, txtHeight, txtWeight, txtBmi, txtUserNameProfile;

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        init();
        setUpParam();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpParam();
    }

    /**
     * Initialize param
     */
    private void init(){
        txtUserName = findViewById(R.id.txtUserName);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        txtBmi = findViewById(R.id.txtBmi);
        btnEdit = findViewById(R.id.btnEditUser);
        txtUserNameProfile = findViewById(R.id.userNameProfile);
    }

    /**
     * Set text and listener for param
     */
    private void setUpParam(){
        if (userService.getUser() != null){
            txtUserNameProfile.setText(userService.getUser().getUserName());
            txtUserName.setText(userService.getUser().getUserName());
            txtHeight.setText(Double.toString(userService.getUser().getHeight()));
            txtWeight.setText(Double.toString(userService.getUser().getWeight()));
            txtBmi.setText(Double.toString(userService.getUser().getBmi()));
        }
        btnEdit.setOnClickListener(view -> {
            Intent intent =new Intent(this, EditUserActivity.class);
            startActivity(intent);
        });
    }
}
