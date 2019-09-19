package com.thinh.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thinh.foodnutrientfact.model.FatInfo;
import com.thinh.foodnutrientfact.model.FatType;
import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.model.VitaminInfo;
import com.thinh.foodnutrientfact.model.VitaminType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class CalorieSettingDAO {
    private SQLiteOpenHelper openHelper;

    @Inject
    public CalorieSettingDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert Calorie Setting based on the amount
     * @param amount calorie amount entered by user
     */
    public boolean insertCalorieSetting(Double amount){

        String query = "SELECT * from calorie_setting";
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            ContentValues contentValues = new ContentValues();
            contentValues.put("amount",amount);
            long result = db.insert("calorie_setting", null, contentValues);
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    cursor.getCount();
                    cursor.getString(cursor.getColumnIndex("amount"));
                } while (cursor.moveToNext());
            }
          return (result == -1)? false : true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
