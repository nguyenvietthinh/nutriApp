package com.tma.techday.foodnutrientfact.viewholder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.activity.AddToCartActivity;
import com.tma.techday.foodnutrientfact.gui.event.CaloriesChangeEvent;
import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.model.WeightUnit;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CartItem extends Fragment {
    private Order order;
    private TextView foodNameItemView, calAmountItemView;
    private EditText foodWeightText;
    Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private ImageButton btnDeleteOrder;
    public CartItem(Order order) {
        this.order = order;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_item_layout,container,false);
        setUpParam(view);

        return view;
    }

    /**
     * Declare Params
     */
    private void setUpParam(View view){
        foodNameItemView = view.findViewById(R.id.cart_item_food_name);
        calAmountItemView = view.findViewById(R.id.cart_item_cal_amount);
        foodWeightText = view.findViewById(R.id.txtFoodWeight);
        btnDeleteOrder = view.findViewById(R.id.btnDeleteOrder);
        NumberFormat numberFormat = new DecimalFormat("0.00");
        foodWeightText.setText(numberFormat.format(order.getFoodWeight()));
        foodNameItemView.setText(order.getFoodName());
        calAmountItemView.setText(numberFormat.format(order.getCalorieAmount()));
        spinner =  view.findViewById(R.id.planets_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weight_unit_array, android.R.layout.simple_spinner_item);//Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String unit = spinner.getSelectedItem().toString();
                WeightUnit weightUnit = WeightUnit.fromDescription(unit);
                //TODO: log weightUnit amount to file
                Log.i("WeightUnit", weightUnit.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnDeleteOrder.setOnClickListener(view1 -> {
            EventBus.getDefault().post(order);
        });
        foodWeightText.setOnFocusChangeListener(setOnFocusFoodWeightText(numberFormat));
    }

    /**
     * set on focus change listener for food weight edit text
     * @param numberFormat
     * @return
     */
    private View.OnFocusChangeListener setOnFocusFoodWeightText(NumberFormat numberFormat) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    foodWeightText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if(TextUtils.isEmpty(foodWeightText.getText().toString())) {
                                Toast.makeText(getActivity(),"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
                                calAmountItemView.setText(numberFormat.format(0.0));
                            }else {
                                Double calAmount = order.getCalorieAmount();
                                Double newWeightfood = Double.parseDouble(foodWeightText.getText().toString());
                                Double newCalAmount = newWeightfood * calAmount / (order.getFoodWeight());
                                Double calChange = newCalAmount - calAmount;
                                order.setCalorieAmount(newCalAmount);
                                order.setFoodWeight(newWeightfood);
                                calAmountItemView.setText(numberFormat.format(order.getCalorieAmount()));
                                EventBus.getDefault().post(new CaloriesChangeEvent(calChange));
                            }
                        }
                    });// code to execute when EditText loses focus
                }
            }
        };
    }

}
