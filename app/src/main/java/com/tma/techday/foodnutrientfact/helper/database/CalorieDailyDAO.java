package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.model.CalorieDaily;
import com.tma.techday.foodnutrientfact.model.Order;

import javax.inject.Inject;

public class CalorieDailyDAO {
    private SQLiteOpenHelper openHelper;
    public static final String TABLE_NAME = "calorie_daily";

    @Inject
    public CalorieDailyDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert Calorie Daily to DB
     * @param calorieDaily
     * @return
     */
    public boolean addCalDaily(CalorieDaily calorieDaily){

        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("date",calorieDaily.getDate());
            contentValues.put("calorie_amount_daily",calorieDaily.getCalorieAmountDaily());
            long result = db.insert(TABLE_NAME, null, contentValues);
            return (result == -1)? false : true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
