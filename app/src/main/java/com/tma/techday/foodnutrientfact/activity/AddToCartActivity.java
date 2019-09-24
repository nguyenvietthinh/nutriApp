package com.tma.techday.foodnutrientfact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.di.FoodNutriApplication;
import com.tma.techday.foodnutrientfact.gui.event.CaloriesChangeEvent;
import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.viewholder.CartAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddToCartActivity extends AppCompatActivity  {
    EditText txtCalSetting;
    public  TextView totalCalView;
    Button btnAdd;
    double total = 0.0;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;
    NumberFormat numberFormat = new DecimalFormat("0.00");

    @Inject
    OrderService orderService;

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
        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        totalCalView = findViewById(R.id.txtTotal);
    }

    /**
     * set on click listener for button save
     * @return
     */
    private View.OnClickListener setupBtnAddOnClickListener() {
        return view -> {
            if(cart.size()>0){
                Toast.makeText(AddToCartActivity.this,"Save successfully.", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(AddToCartActivity.this,"Your cart is empty.", Toast.LENGTH_LONG).show();
            String calAmount = txtCalSetting.getText().toString();
            //TODO: log raw amount to file
            int c = Log.i("AmountAddToCart", calAmount);
            if(TextUtils.isEmpty(calAmount)){
                Toast.makeText(AddToCartActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(AddToCartActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
            }

        };
    }


    /**
     * Load list order to cart
     */
    private void loadCart() {
        cart = orderService.getOrderList();
        cartAdapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        for(Order order: cart){
            total+= order.getCalorieAmount();
        }
        totalCalView.setText(numberFormat.format(total));
    }

    /**
     * Delete selected item of cart
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Delete")){
            deleteCart(item.getOrder());
        }
        return true;
    }

    /**
     * Delete Item of cart
     * @param position
     */
    private void deleteCart(int position) {
        cart.remove(position);
        orderService.clearCart();
        for(Order order:cart){
            orderService.addOrderToCard(order);
        }
        loadCart();
    }

    /**
     * Receive calorie changed from adapter
     */
    @Subscribe
    public void onEvent(CaloriesChangeEvent calChange){
        total += calChange.getCaloriesChange();
        totalCalView.setText(numberFormat.format(total));
    }
}
