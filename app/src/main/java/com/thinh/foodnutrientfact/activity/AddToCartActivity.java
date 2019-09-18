package com.thinh.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;
import com.thinh.foodnutrientfact.model.UnitCalorie;
import com.thinh.foodnutrientfact.service.CalorieSettingService;
import com.thinh.foodnutrientfact.service.FoodNutriService;

import javax.inject.Inject;

public class AddToCartActivity extends AppCompatActivity {
    EditText txtCalSetting;
    Button btnSave;
    Spinner spinner;
    UnitCalorie selectedCalUnit;
    String calAmount;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart);
        setUpParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);   // Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
//        spinner.setOnItemClickListener((adapterView, view, i, l) -> {
//            selectedCalUnit = (UnitCalorie) adapterView.getItemAtPosition(i);
//        });

//        btnSave.setOnClickListener(view -> {
//            if(TextUtils.isEmpty(calAmount)){
//                calorieSettingService.insertCalorieSetting(calAmount,selectedCalUnit.name());
//                Toast.makeText(AddToCartActivity.this,"Data Inserted Successfully", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(AddToCartActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
//
//            }
//        });
    }

    /**
     * Declare Params
     */
    private void setUpParam(){
         spinner =  findViewById(R.id.planets_spinner);
         btnSave = findViewById(R.id.btnAdd);
         txtCalSetting = findViewById(R.id.txtCalSetting);
    }
}
