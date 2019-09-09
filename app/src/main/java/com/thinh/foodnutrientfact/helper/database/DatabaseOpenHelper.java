package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.sql.Struct;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "food_nutri_fact_db.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
}
