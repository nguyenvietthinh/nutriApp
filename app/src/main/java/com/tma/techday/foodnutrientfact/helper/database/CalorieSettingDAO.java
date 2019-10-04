package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.model.CalorieSetting;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

public class CalorieSettingDAO {
    private SQLiteOpenHelper openHelper;
    public static final String TABLE_NAME = "calorie_setting";
    @Inject
    public CalorieSettingDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert Calorie Setting based on the amount
     * @param calorieSetting entered by user
     */
    public boolean insertCalorieSetting(CalorieSetting calorieSetting){


        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(calorieSetting.getDate());
            ContentValues contentValues = new ContentValues();
            contentValues.put("date",date);
            contentValues.put("calorie_setting_amount",calorieSetting.getCalorieSettingAmount());
            long result = db.insert(TABLE_NAME, null, contentValues);
            return (result == -1)? false : true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get calorie setting of user
     * @return
     */
    public CalorieSetting getCalorieSetting(){
        String query = "SELECT * from "+TABLE_NAME+"";
        CalorieSetting calorieSetting = null;
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    double calorieSettingAmount = Double.parseDouble( cursor.getString(cursor.getColumnIndex("calorie_setting_amount")));
                    calorieSetting = CalorieSetting.of(new Date(),calorieSettingAmount);
                } while (cursor.moveToNext());
                return calorieSetting;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return calorieSetting;
    }

    /**
     * Update calorie setting in the same day
     * @param calorieSetting
     */
    public void updateCalorieSetting(CalorieSetting calorieSetting){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateCalSetting = sdf.format(calorieSetting.getDate());
        try (SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("calorie_setting_amount", calorieSetting.getCalorieSettingAmount());
            db.update(TABLE_NAME, contentValues, "date LIKE ?",new String[] { "%" + dateCalSetting + "%" });
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
