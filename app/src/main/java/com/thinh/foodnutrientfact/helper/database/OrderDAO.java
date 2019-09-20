package com.thinh.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thinh.foodnutrientfact.model.Order;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class OrderDAO {
    private SQLiteOpenHelper openHelper;
    public static final String TABLE_NAME = "order_detail";

    @Inject
    public OrderDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert Order to DB
     * @param order
     * @return
     */
    public boolean insertCalorieSetting(Order order){


        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("food_name",order.getFoodName());
            contentValues.put("calorie_amount",order.getCalorieAmount());
            contentValues.put("food_weight",order.getFoodWeight());
            long result = db.insert(TABLE_NAME, null, contentValues);
            return (result == -1)? false : true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get cart from database
     * @return
     */
    public List<Order> getCarts(){
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * from "+TABLE_NAME+"";
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{})){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    orders.add(new Order(cursor.getString(cursor.getColumnIndex("food_name")),Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount"))),Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount")))));
                } while (cursor.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    public void clearCart(){
        String query = "Delete from "+TABLE_NAME+"";

    }

}
