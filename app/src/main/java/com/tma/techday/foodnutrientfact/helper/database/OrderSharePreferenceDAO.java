package com.tma.techday.foodnutrientfact.helper.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.model.Order;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderSharePreferenceDAO  {


    public static final String orderpreference = "orderpref";
    public static final String ORDERS = "orders";
      SharedPreferences sharedpreferences;
      Gson gson = new Gson();
      Context mcontext;


    public OrderSharePreferenceDAO(Context context) {
        this.sharedpreferences = context.getSharedPreferences(orderpreference, context.MODE_PRIVATE);
        mcontext = context;
    }



        //use the value of i where needed.






    public  boolean addOrderToCard(Order order){


        String json = gson.toJson(order);
        sharedpreferences.edit().putString(order.getFoodName(), json).commit();
        return true;
    }

    public List<Order> getCart(){
        List<Order> orders = new ArrayList<>();
        if (sharedpreferences != null) {
            Map<String,?> entries = sharedpreferences.getAll();
            Set<String> keys = entries.keySet();
            Gson gson = new Gson();
            for (String key : keys) {
                String json = sharedpreferences.getString(key, "");
                Order order = gson.fromJson(json, Order.class);
                orders.add(order);
            }
        }

        return orders;
    }

    public void clearCart(){

        sharedpreferences.edit().clear().commit();
    }

    public int getCountCart(){

        int count = 0;
        if (sharedpreferences != null) {
            count = sharedpreferences.getAll().size();
        }

        return count;
    }
}
