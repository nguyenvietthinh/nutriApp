package com.tma.techday.foodnutrientfact.helper.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tma.techday.foodnutrientfact.model.Order;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderSharePreferenceDAO extends Fragment {


    public static final String orderpreference = "orderpref";
    public static final String foodName = "foodName";
    public static final String calorieAmount = "calorieAmount";
    public static final String foodWeight = "foodWeight";
    public static final String ORDERS = "orders";
    List<Order> orders = new ArrayList<>();
    Gson gson = new Gson();
    SharedPreferences sharedpreferences = getActivity().getSharedPreferences(orderpreference,
            Context.MODE_PRIVATE);



    public boolean addOrderToCard(Order order){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        orders.add(order);
        String json = gson.toJson(orders);
        editor.putString(ORDERS, json);
        editor.commit();
        return true;
    }

    public List<Order> getCart(){
        String json = sharedpreferences.getString(ORDERS, "");
        Type type = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(json, type);
        return orders;
    }
}
