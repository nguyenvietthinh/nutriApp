package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import javax.inject.Inject;
import javax.inject.Named;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "food_nutri_fact_db.db";
    private static final String DATABASE_STORAGE_DIRECTORY = "/data/user/0/com.thinh.filebaseimagelabeling/databases";
    private static final int DATABASE_VERSION = 1;


    public DatabaseOpenHelper() {
       super(null,DATABASE_NAME, DATABASE_STORAGE_DIRECTORY,null,DATABASE_VERSION);
    }

    @Inject
    public DatabaseOpenHelper(@Named("ApplicationContext") Context context) {
        super(context, DATABASE_NAME, DATABASE_STORAGE_DIRECTORY, null, DATABASE_VERSION);
    }
}
