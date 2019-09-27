package com.tma.techday.foodnutrientfact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.gui.dialog.CalorieComparisionDialog;
import com.tma.techday.foodnutrientfact.gui.event.CaloriesChangeEvent;
import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.service.CalorieDailyService;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.viewholder.CartItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class AddToCartActivity extends AppCompatActivity {
    EditText txtCalSetting;
    public  TextView totalCalView;
    Button btnAdd;
    ImageButton btnResearch;
    double total = 0.0;
    List<Order> cart = new ArrayList<>();
    NumberFormat numberFormat = new DecimalFormat("0.00");
    LinearLayout layOutOrderItems;

    @Inject
    OrderService orderService;

    @Inject
    CalorieDailyService calorieDailyService;

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
        loadCart();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /**
     * Declare Params
     */
    private void setUpParam(){
        btnAdd = findViewById(R.id.btnSave);
        txtCalSetting = findViewById(R.id.txtCalSetting);
        btnAdd.setOnClickListener(setupBtnAddOnClickListener());
        layOutOrderItems = findViewById(R.id.layout_order_items);
        totalCalView = findViewById(R.id.txtTotal);
        btnResearch = findViewById(R.id.btnResearch);

        btnResearch.setOnClickListener(setBtnResearchOnClickListener());
    }

    /**
     * set onclick listener for image button research
     * @return
     */
    private View.OnClickListener setBtnResearchOnClickListener() {
        return view -> {
//                showCalComparisionDialog();
            CalorieDaily calorieDaily = buildCalorieDaily();
            Intent intent =new Intent(this, CalorieComparisionActivity.class);
            intent.putExtra("CalorieDaily", calorieDaily);
            startActivity(intent);
            };
    }

    /**
     * show calorie comparision dialog
     */
    private void showCalComparisionDialog() {
        CalorieComparisionDialog calorieComparisionDialog = new CalorieComparisionDialog();
        Bundle args = new Bundle();   //Use bundle to pass data
        CalorieDaily calorieDaily = buildCalorieDaily();
        args.putSerializable("calDaily", calorieDaily);
        calorieComparisionDialog.setArguments(args);
        calorieComparisionDialog.show(getSupportFragmentManager(),"dialog");

    }

    /**
     * set on click listener for button save
     * @return
     */
    private View.OnClickListener setupBtnAddOnClickListener() {
        return view -> {
            boolean hasItems = cart.size() > 0;
            if(hasItems) {
                CalorieDaily calorieDaily = buildCalorieDaily();
                if (calorieDailyService.addCalDaily(calorieDaily)) {
                    layOutOrderItems.removeAllViews();
                    totalCalView.setText(numberFormat.format(0.0));
                    orderService.clearCart();
                    Toast.makeText(AddToCartActivity.this,"Save successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(this, CalorieComparisionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddToCartActivity.this, "Save failed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AddToCartActivity.this,"Your cart is empty.", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Build Calorie Daily
     * @return
     */
    private CalorieDaily buildCalorieDaily() {
        return new CalorieDaily(new Date(),Math.round(total * 100.0) / 100.0);
    }


    /**
     * Load list order to cart
     */
    private void loadCart() {
        cart = orderService.getOrderList();
        if(cart.size()!= 0) {
            for (Order order : cart) {
                Fragment cartItemFragment = new CartItem(order);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.layout_order_items, cartItemFragment);
                transaction.commit();
                total += order.getCalorieAmount();
            }
        }else {
            total = 0.0;
        }
        totalCalView.setText(numberFormat.format(total));
    }



    /**
     * Receive calorie changed from adapter
     */
    @Subscribe
    public void onEvent(CaloriesChangeEvent calChange){
        total += calChange.getCaloriesChange();
        totalCalView.setText(numberFormat.format(total));
    }

    /**
     * Delete Item of cart
     * @param order
     */
    @Subscribe(priority = 1)
    public void onEvent(Order order){
        cart.remove(order);
        orderService.clearCart();
        layOutOrderItems.removeAllViews();
        for(Order orderInCart:cart){
            orderService.addOrderToCard(orderInCart);
        }
        loadCart();
    }


}
