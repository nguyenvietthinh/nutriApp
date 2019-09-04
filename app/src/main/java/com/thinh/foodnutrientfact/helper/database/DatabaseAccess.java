package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private static final String TABLE_NAME = "food_nutri";
    Cursor cursor = null;

    /**
     * private constructor so that object creation from outside the class is avoided
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * return the single instance of databases
     * @param context
     */
    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * open connection databases
     */
//
//    public void open(){
//        this.db = openHelper.getWritableDatabase();
//    }

    /**
     * close the databases
     */
    public void close(){
        if(db!=null){
            this.db.close();
        }
    }

    /**
     * get Food Nutrition from Food Name
     * @param foodName
     * @return
     */
    public String getFoodNutri(String foodName){

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Shrt_Desc LIKE '%"+foodName+"%'";
        StringBuffer buffer = new StringBuffer();
        try {
             db = openHelper.getWritableDatabase();
            cursor = db.rawQuery(query, new String[]{});
            // Move to first row
            if (cursor.moveToFirst()) {
                do {

                    buffer.append("Water: "+ cursor.getString(2)+"n");
                    buffer.append("Energ_Kcal: "+ cursor.getString(3)+"n");
                    buffer.append("Protein: "+ cursor.getString(4)+"n");
                    buffer.append("Lipid_Tot: "+ cursor.getString(5)+"n");

                } while (cursor.moveToNext());
                return buffer.toString();
            }
        }
        finally{
            cursor.close();
            db.close();
        }

        return buffer.toString();
    }
}
