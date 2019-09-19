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

    @Inject
    public OrderDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert order to DB
     * @param foodName
     * @param calAmount
     * @param foodWeight
     * @return
     */
    public boolean insertCalorieSetting(String foodName,double calAmount, double foodWeight){


        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("food_name",foodName);
            contentValues.put("calorie_amount",calAmount);
            contentValues.put("food_weight",foodWeight);
            long result = db.insert("order_detail", null, contentValues);
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
        String query = "SELECT * from order_detail";
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
}
