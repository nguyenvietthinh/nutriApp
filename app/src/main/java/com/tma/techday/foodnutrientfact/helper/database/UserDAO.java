package com.tma.techday.foodnutrientfact.helper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.enums.Gender;
import com.tma.techday.foodnutrientfact.model.User;

import javax.inject.Inject;

public class UserDAO {

    private SQLiteOpenHelper openHelper;
    public static final String TABLE_NAME = "user";

    @Inject
    public UserDAO(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    /**
     * Insert user
     * @param user entered by UserActivity
     */
    public boolean insertUser(User user){

        try(SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",user.getUserName());
            contentValues.put("height",user.getHeight());
            contentValues.put("weight",user.getWeight());
            contentValues.put("bmi",user.getBmi());
            contentValues.put("age",user.getAge());
            contentValues.put("gender",user.getGender().toString());
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
    public User getUser(){
        String query = "SELECT * from "+TABLE_NAME+"";
        User user = null;
        try(SQLiteDatabase db = openHelper.getWritableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{ })){
            if (cursor.moveToFirst()) { // Move to first row
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    double height = Double.parseDouble( cursor.getString(cursor.getColumnIndex("height")));
                    double weight = Double.parseDouble( cursor.getString(cursor.getColumnIndex("weight")));
                    double bmi = Double.parseDouble( cursor.getString(cursor.getColumnIndex("bmi")));
                    int age = Integer.parseInt( cursor.getString(cursor.getColumnIndex("age")));
                    Gender gender = null;
                    if(Gender.Male.toString().equals(cursor.getString(cursor.getColumnIndex("gender")))){
                        gender = Gender.Male;
                    }else if (Gender.Female.toString().equals(cursor.getString(cursor.getColumnIndex("gender")))){
                        gender = Gender.Female;
                    }
                    user = User.of(name,height,weight,bmi,age,gender);
                } while (cursor.moveToNext());
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Get count row of user
     * @return
     */
    public int getProfilesCount() {
        int count = 0;
        try (SQLiteDatabase db = openHelper.getWritableDatabase()){
             count = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            return count;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Update user
     * @param user
     */
    public void updateUser(User user){
        try (SQLiteDatabase db = openHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", user.getUserName());
            contentValues.put("height", user.getHeight());
            contentValues.put("weight", user.getWeight());
            contentValues.put("bmi", user.getBmi());
            contentValues.put("age",user.getAge());
            contentValues.put("gender",user.getGender().toString());
            db.update(TABLE_NAME, contentValues, "",new String[] {});
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
