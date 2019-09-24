package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.model.Order;

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
    public boolean addOrderToCard(Order order){


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
    public List<Order> getOrderList(){
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * from "+TABLE_NAME+"";
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    String foodName = cursor.getString(cursor.getColumnIndex("food_name"));
                    double calAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("calorie_amount")));
                    double foodWeight = Double.parseDouble(cursor.getString(cursor.getColumnIndex("food_weight")));
                    orders.add(new Order(foodName,calAmount,foodWeight));
                } while (cursor.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Delete all order from cart
     * @return
     */
    public void clearCart(){
        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
             db.delete(TABLE_NAME, null, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Get total item of cart
     * @return
     */
    public int getCountCart(){
        int count = 0;
        String query = "SELECT count(*) from "+TABLE_NAME+"";
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

}
