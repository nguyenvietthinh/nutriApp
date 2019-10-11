package com.tma.techday.foodnutrientfact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.model.User;
import com.tma.techday.foodnutrientfact.service.UserService;
import javax.inject.Inject;

/**
 * Create user if not created before
 * Edit user information
 */
public class EditUserActivity extends AppCompatActivity {

    TextView txtEditUserName, txtEditHeight, txtEditWeight;
    Button btnSave;

    @Inject
    UserService userService;

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
        btnSave = findViewById(R.id.btnEditUserSave);
        if (userService.getUser() != null){
            txtEditUserName.setText(userService.getUser().getUserName());
            txtEditHeight.setText(Double.toString(userService.getUser().getHeight()));
            txtEditWeight.setText(Double.toString(userService.getUser().getWeight()));
        }
        btnSave.setOnClickListener(view -> {
            if (userService.getProfilesCount() > 0){
                userService.updateUser(buildUser());
                Toast.makeText(EditUserActivity.this, getString(R.string.update_user), Toast.LENGTH_LONG).show();
            }else {
                userService.insertUser(buildUser());
                Toast.makeText(EditUserActivity.this, getString(R.string.save_successfully), Toast.LENGTH_LONG).show();
                Intent intent =new Intent(this, MainActivity.class);
                startActivity(intent);
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
        return User.of(userName,height,weight,bmi);
    }
}
