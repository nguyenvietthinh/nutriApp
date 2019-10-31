package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.enums.Gender;
import com.tma.techday.foodnutrientfact.model.CalorieSetting;
import com.tma.techday.foodnutrientfact.model.User;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.UserService;

import java.util.Date;

import javax.inject.Inject;

/**
 * Create user if not created before
 * Edit user information
 */
public class EditUserActivity extends AppCompatActivity {

    TextView txtEditUserName, txtEditHeight, txtEditWeight,txtEditAge;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Button btnSave;

    @Inject
    UserService userService;

    @Inject
    CalorieSettingService calorieSettingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
        setUpParam();
    }

    /**
     * Declare Params and set on click listener for buttons
     */
    private void setUpParam(){
        txtEditUserName = findViewById(R.id.txtEditUserName);
        txtEditHeight = findViewById(R.id.txtEditHeight);
        txtEditWeight = findViewById(R.id.txtEditWeight);
        txtEditAge = findViewById(R.id.txtEditAge);
        btnSave = findViewById(R.id.btnEditUserSave);
        radioSexGroup = findViewById(R.id.radioSex);
        if (userService.getUser() != null){
            txtEditUserName.setText(userService.getUser().getUserName());
            txtEditHeight.setText(Double.toString(userService.getUser().getHeight()));
            txtEditWeight.setText(Double.toString(userService.getUser().getWeight()));
            txtEditAge.setText(Integer.toString(userService.getUser().getAge()));
            if (Gender.Male.equals(userService.getUser().getGender())){
                radioSexButton = findViewById(R.id.radioMale);
                radioSexButton.setChecked(true);
            }else if (Gender.Female.equals(userService.getUser().getGender())){
                radioSexButton = findViewById(R.id.radioFemale);
                radioSexButton.setChecked(true);
            };
        }
        btnSave.setOnClickListener(view -> {
            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioSexButton = (RadioButton) findViewById(selectedId);
            if (Integer.valueOf(txtEditAge.getText().toString())<15 ||Integer.valueOf(txtEditAge.getText().toString())>80){
                Toast.makeText(EditUserActivity.this, getString(R.string.error_age), Toast.LENGTH_LONG).show();
            }else {
                if (userService.getProfilesCount() > 0){
                    userService.updateUser(buildUser());
                    calorieSettingService.updateCalorieSetting(buildCalorieSetting(buildUser()));
                    Toast.makeText(EditUserActivity.this, getString(R.string.update_user), Toast.LENGTH_LONG).show();
                }else {

                    userService.insertUser(buildUser());
                    calorieSettingService.insertCalorieSetting(buildCalorieSetting(buildUser()));
                    Toast.makeText(EditUserActivity.this, getString(R.string.save_successfully), Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Build user
     * @return
     */
    private User buildUser() {
        String userName = txtEditUserName.getText().toString();
        Double height = Double.valueOf(txtEditHeight.getText().toString());
        Double weight = Double.valueOf(txtEditWeight.getText().toString());
        Double mheight = height/100.0;
        Double bmi =  weight/(mheight*mheight);
        Integer age = Integer.valueOf(txtEditAge.getText().toString());
        Gender gender = null;
        if(Gender.Male.toString().equals(radioSexButton.getText())){
            gender = Gender.Male;
        }else if (Gender.Female.toString().equals(radioSexButton.getText())){
            gender = Gender.Female;
        }
        return User.of(userName,height,weight,bmi,age,gender);
    }

    /**
     * Build calorie setting
     * @return
     */
    private CalorieSetting buildCalorieSetting(User user){
        Double bmr = 0.0;
        if(Gender.Male.equals(user.getGender())){
            bmr = 66 + (13.7*user.getWeight()) + (5*user.getHeight()) - (6.76 * user.getAge());

        }else if (Gender.Female.equals(user.getGender())){
            bmr = 655 + (9.6*user.getWeight()) + (1.8*user.getHeight()) - (4.7*user.getAge());
        }
        return CalorieSetting.of(new Date(),bmr);
    }
}
