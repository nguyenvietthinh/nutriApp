package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.helper.SystemConstant;
import com.tma.techday.foodnutrientfact.model.CalorieDaily;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        try (SQLiteDatabase db = openHelper.getWritableDatabase()) {
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
            String date = sdf.format(calorieDaily.getDate());
            ContentValues contentValues = new ContentValues();
            contentValues.put("date",date);
            contentValues.put("calorie_daily_amount",calorieDaily.getCalorieDailyAmount());
            contentValues.put("protein_daily_amount",calorieDaily.getProteinDailyAmount());
            contentValues.put("fat_daily_amount",calorieDaily.getFatDailyAmount());
            long result = db.insert(TABLE_NAME, null, contentValues);
            return (result == -1)? false : true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get calorie setting of UserActivity
     * @return
     */
    public List<CalorieDaily> getCalorieDaily(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
        String dateCalDaily = sdf.format(date);
        String query = "SELECT * from calorie_daily WHERE date  LIKE ?";
        CalorieDaily calorieDaily = null;
        List<CalorieDaily> calorieDailyList = new ArrayList<>();
        try (SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ "%" + dateCalDaily + "%" })) {
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    double calorieDailyAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("calorie_daily_amount")));
                    double proteinDailyAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("protein_daily_amount")));
                    double fatDailyAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("fat_daily_amount")));
                    calorieDaily = CalorieDaily.of(new Date(),calorieDailyAmount, proteinDailyAmount, fatDailyAmount);
                    calorieDailyList.add(calorieDaily);
                } while (cursor.moveToNext());
                return calorieDailyList;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return calorieDailyList;
    }

}
