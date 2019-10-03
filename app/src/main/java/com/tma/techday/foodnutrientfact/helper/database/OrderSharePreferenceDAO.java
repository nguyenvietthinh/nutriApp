package com.tma.techday.foodnutrientfact.helper.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.tma.techday.foodnutrientfact.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderSharePreferenceDAO  {


    public static final String orderpreference = "orderpref";
    SharedPreferences sharedpreferences;
    Gson gson = new Gson();
    Context mcontext;


    public OrderSharePreferenceDAO(Context context) {
        this.sharedpreferences = context.getSharedPreferences(orderpreference, context.MODE_PRIVATE);
        mcontext = context;
    }

    /**
     * Add order to cart
     * @param order from
     * @return
     */
    public boolean addOrderToCard(Order order){
        String json = gson.toJson(order);
        sharedpreferences.edit().putString(order.getFoodName(), json).commit();
        return true;
    }

    /**
     * Get list orders
     * @return
     */
    public List<Order> getCart(){
        List<Order> orders = new ArrayList<>();
        if (sharedpreferences != null) {
            Map<String,?> entries = sharedpreferences.getAll(); //  get all of orderpreference share preferences
            Set<String> keys = entries.keySet(); // get all set key of share preferences
            Gson gson = new Gson();
            for (String key : keys) {
                String value = sharedpreferences.getString(key, "");
                Order order = gson.fromJson(value, Order.class);
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * Clear all item from cart
     */
    public void clearCart(){
        sharedpreferences.edit().clear().commit();
    }

    /**
     * Get item number of cart
     * @return
     */
    public int getCountCart(){
        int count;
        count =(sharedpreferences != null)? sharedpreferences.getAll().size():0;
        return count;
    }
}
