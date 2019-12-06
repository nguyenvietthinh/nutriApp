package com.tma.techday.foodnutrientfact.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.gui.event.CaloriesChangeEvent;
import com.tma.techday.foodnutrientfact.gui.event.DeleteOrderEvent;
import com.tma.techday.foodnutrientfact.model.Order;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Contains and resolve operations on the cart item
 */
public class CartItem extends Fragment {
    private Order order;
    private Double calAmountFinal;
    private Double proteinAmountFinal;
    private Double fatAmountFinal;
    private Double foodWeightFinal;
    private TextView foodNameItemView, calAmountItemView;
    private EditText foodWeightText;
    private ImageButton btnDeleteOrder;

    public CartItem(Order order, Double calAmountFinal, Double proteinAmountFinal, Double fatAmountFinal, Double foodWeightFinal) {
        this.order = order;
        this.calAmountFinal = calAmountFinal;
        this.proteinAmountFinal = proteinAmountFinal;
        this.fatAmountFinal = fatAmountFinal;
        this.foodWeightFinal = foodWeightFinal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_item_layout,container,false);
        setUpParam(view);
        return view;
    }

    /**
     * Declare Params, set Text and set on click listener.
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

        btnDeleteOrder.setOnClickListener(view1 -> {
            buildDialogConfirm();
        });
        foodWeightText.setOnFocusChangeListener(setOnFocusFoodWeightText(numberFormat));
    }

    /**
     * Set on focus change listener for food weight edit text
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

                            if (TextUtils.isEmpty(foodWeightText.getText().toString())) {
                                Toast.makeText(getActivity(),getString(R.string.required_empty_text), Toast.LENGTH_LONG).show();
                                foodWeightText.setText(numberFormat.format(100.0));
                                calAmountItemView.setText(numberFormat.format((100.0 * calAmountFinal) / foodWeightFinal)); // Calorie of 100 gram food
                            } else {
                                try {
                                    NumberFormat format = NumberFormat.getInstance();
                                    Double newWeightfood = format.parse(foodWeightText.getText().toString()).doubleValue();
                                    Double calAmount  = order.getCalorieAmount();
                                    Double proteinAmount  = order.getProteinAmount();
                                    Double fatAmount = order.getFatAmount();
                                    Double newCalAmount = 0.0;
                                    Double newProteinAmount = 0.0;
                                    Double newFatAmount = 0.0;
                                    if (Double.compare(newWeightfood,0.0) == 0 ) {
                                        newCalAmount = 0.0;
                                        order.setCalorieAmount(newCalAmount);
                                        newProteinAmount = 0.0;
                                        order.setProteinAmount(newProteinAmount);
                                        newFatAmount = 0.0;
                                        order.setFatAmount(newFatAmount);
                                        foodNameItemView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info, 0);
                                    } else {
                                        newCalAmount =(newWeightfood * calAmountFinal ) / foodWeightFinal;
                                        newProteinAmount =(newWeightfood * proteinAmountFinal ) / foodWeightFinal;
                                        newFatAmount =(newWeightfood * fatAmountFinal ) / foodWeightFinal;
                                        foodNameItemView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        order.setCalorieAmount(newCalAmount);
                                        order.setProteinAmount(newProteinAmount);
                                        order.setFatAmount(newFatAmount);
                                        order.setFoodWeight(newWeightfood);
                                    }
                                    Double calChange = newCalAmount - calAmount;
                                    Double proteinChange = newProteinAmount - proteinAmount;
                                    Double fatChange = newFatAmount - fatAmount;
                                    calAmountItemView.setText(numberFormat.format(order.getCalorieAmount()));
                                    EventBus.getDefault().post(new CaloriesChangeEvent(calChange,proteinChange,fatChange));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };
    }

    /**
     * Create confirm dialog delete cart item
     */
    private void buildDialogConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirm_dialog_title));
        builder.setMessage(getString(R.string.confirm_dialog_content));
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new DeleteOrderEvent(order));
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
