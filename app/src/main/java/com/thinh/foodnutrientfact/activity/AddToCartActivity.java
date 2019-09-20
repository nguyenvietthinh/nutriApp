package com.thinh.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.di.FoodNutriApplication;
import com.thinh.foodnutrientfact.model.WeightUnit;

public class AddToCartActivity extends AppCompatActivity  {
    EditText txtCalSetting;
    Button btnAdd;
    Spinner spinner;
    WeightUnit weightUnit;


    /**
     * <ul>
     *     <li>Create spinner and innit param for Add to cart</li>
     *     <li> Get data from edit text and spinner</li>
     *     <li>Save information to DB </li>
     * </ul>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart);
        setUpParam();
        FoodNutriApplication application = (FoodNutriApplication) getApplication();
        application.getComponent().inject(this);
    }

    /**
     * Declare Params
     */
    private void setUpParam(){
        spinner =  findViewById(R.id.planets_spinner);
        btnAdd = findViewById(R.id.btnAdd);
        txtCalSetting = findViewById(R.id.txtCalSetting);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weight_unit_array, android.R.layout.simple_spinner_item);   //Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
        spinner.setOnItemSelectedListener(setupOnItemSelectedSpinnerListener());
        btnAdd.setOnClickListener(setupBtnAddOnClickListener());
    }

    /**
     * set on click listener for button add
     * @return
     */
    private View.OnClickListener setupBtnAddOnClickListener() {
        return view -> {
            String calAmount = txtCalSetting.getText().toString();
            //TODO: log raw amount to file
            int c = Log.i("AmountAddToCart", calAmount);
            if(TextUtils.isEmpty(calAmount)){
                Toast.makeText(AddToCartActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(AddToCartActivity.this,"Data Inserted "+weightUnit+" ", Toast.LENGTH_LONG).show();

            }
        };
    }

    /**
     *Set on item selected listener for spinner
     * @return
     */
    private AdapterView.OnItemSelectedListener setupOnItemSelectedSpinnerListener() {
        return new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String unit = spinner.getSelectedItem().toString();
                    weightUnit = WeightUnit.fromDescription(unit);
                    //TODO: log weightUnit amount to file
                    Log.i("WeightUnit", weightUnit.toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            };
    }


}
