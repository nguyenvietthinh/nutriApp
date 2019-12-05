package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.helper.SystemConstant;
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
     * @param calorieSetting entered by UserActivity
     */
    public boolean insertCalorieSetting(CalorieSetting calorieSetting){


        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
            String date = sdf.format(calorieSetting.getDate());
            ContentValues contentValues = new ContentValues();
            contentValues.put("date",date);
            contentValues.put("calorie_setting_amount",calorieSetting.getCalorieSettingAmount());
            contentValues.put("protein_necess_amount",calorieSetting.getProteinNecessAmount());
            contentValues.put("fat_necess_amount",calorieSetting.getFatNecessAmount());
            long result = db.insert(TABLE_NAME, null, contentValues);
            return (result == -1)? false : true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get calorie setting of UserActivity
     * @return
     */
    public CalorieSetting getCalorieSetting(){
        String query = "SELECT * from "+TABLE_NAME+"";
        CalorieSetting calorieSetting = null;
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    double calorieSettingAmount = Double.parseDouble( cursor.getString(cursor.getColumnIndex("calorie_setting_amount")));
                    double proteinNecessAmount = Double.parseDouble( cursor.getString(cursor.getColumnIndex("protein_necess_amount")));
                    double fatNecessAmount = Double.parseDouble( cursor.getString(cursor.getColumnIndex("fat_necess_amount")));
                    calorieSetting = CalorieSetting.of(new Date(),calorieSettingAmount,proteinNecessAmount,fatNecessAmount);
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
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstant.DATE_FORMAT_YYYY_MM_DD);
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
