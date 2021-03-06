package com.tma.techday.foodnutrientfact.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.helper.SystemConstant;
import com.tma.techday.foodnutrientfact.model.CalorieSetting;
import com.tma.techday.foodnutrientfact.model.User;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * Edit and Save Calorie Setting
 */
public class SettingActivity extends AppCompatActivity {

    EditText txtCalSetting;
    Button btnSave;

    @Inject
    CalorieSettingService calorieSettingService;

    @Inject
    UserService userService;

    /**
     * Save UserActivity's calorie needs to database
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calorie_setting);
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        setUpParam();
    }

    /**
     * Declare Params
     */
    private void setUpParam(){
        btnSave = findViewById(R.id.btnSave);
        txtCalSetting = findViewById(R.id.txtCalSetting);
        btnSave.setOnClickListener(setBtnSaveOnclickListener());
        if (calorieSettingService.getCalorieSetting()!= null){
            txtCalSetting.setText(String.valueOf(calorieSettingService.getCalorieSetting().getCalorieSettingAmount()));
        }
    }

    /**
     * Set onclick listener for button Save
     * @return
     */
    private View.OnClickListener setBtnSaveOnclickListener() {
        return view -> {
            String calAmount = txtCalSetting.getText().toString();
            User user = userService.getUser();
            if (TextUtils.isEmpty(calAmount)) {
                Toast.makeText(SettingActivity.this,getString(R.string.required_empty_text), Toast.LENGTH_LONG).show();
            } else {
                double parseCalories = Double.parseDouble(calAmount);
                Double tdee = parseCalories*1.55; // total daily energy expenditure of people with Moderately Active
                Double fatNecessAmount =  (tdee * 0.25) / 9;
                Double proteinNecessAmount =(user.getWeight() * 3.3);
                CalorieSetting calorieSetting = buildCalorieSetting(parseCalories,proteinNecessAmount,fatNecessAmount);
                if (calorieSetting!= null) {
                    boolean isInserted = calorieSettingService.insertCalorieSetting(calorieSetting);
                    if (isInserted) {
                        Toast.makeText(SettingActivity.this, getString(R.string.save_successfully), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(SettingActivity.this, getString(R.string.save_fail), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
    }

    /**
     * Build Calorie Setting
     * @return
     */
    private CalorieSetting buildCalorieSetting(double calorieSettingAmount,double proteinNecessAmount,double fatNecessAmount) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
        String date = sdf.format(currentDate);
        if (calorieSettingService.getCalorieSetting()!= null) {
            Date dateAdded = calorieSettingService.getCalorieSetting().getDate();
            String dateAdd = sdf.format(dateAdded);
            if (date.equals(dateAdd)) {
                buildDialogConfirm(new CalorieSetting(currentDate, calorieSettingAmount,proteinNecessAmount,fatNecessAmount));
                return null;
            }
        }
        return new CalorieSetting(currentDate,calorieSettingAmount,proteinNecessAmount,fatNecessAmount);
    }

    /**
     * Build Dialog confirm and update calorie setting if choose yes
     * @param calorieSetting
     */
    private void buildDialogConfirm(CalorieSetting calorieSetting){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_dialog_title));
        builder.setMessage(getString(R.string.confirm_dialog_update_calsetting_content));
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calorieSettingService.updateCalorieSetting(calorieSetting);
                Toast.makeText(SettingActivity.this, getString(R.string.update_calorie_setting), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
        builder.show();
    }
}
