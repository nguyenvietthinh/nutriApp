package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.CalorieSetting;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.UserService;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    CardView cardViewDetect, cardViewCalSetting;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private TextView userNameView;
    private View headerLayout;

    @Inject
    UserService userService;

    @Inject
    CalorieSettingService calorieSettingService;

    /**
     * Switch from home screen to calorie setting and detect components
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        setupParam();
        if (!checkUserNameEmpty()){
            doOpenOtherActivity(EditUserActivity.class);
        }



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
        drawerLayout = findViewById(R.id.activity_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navUser);
        headerLayout = navigationView.getHeaderView(0);
        userNameView = headerLayout.findViewById(R.id.userNameNav);
        if (userService.getUser() != null){
            userNameView.setText(userService.getUser().getUserName());
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.account){
                    if (userService.getUser() != null){
                        doOpenOtherActivity(UserActivity.class);
                    }else {
                        Toast.makeText(MainActivity.this, getString(R.string.require_edit_user), Toast.LENGTH_LONG).show();
                        doOpenOtherActivity(UserActivity.class);
                    }
                }else if (id == R.id.settingLanguage){
                        doOpenOtherActivity(SettingLanguageActivity.class);
                }
                return true;

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private boolean checkUserNameEmpty(){
        if (userNameView.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}
