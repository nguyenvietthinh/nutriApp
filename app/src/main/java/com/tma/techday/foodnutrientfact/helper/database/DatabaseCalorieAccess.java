package com.tma.techday.foodnutrientfact.helper.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Connect with sqlite database nutri_app_db
 */
public class DatabaseCalorieAccess extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "nutri_app_db.db";
    private static final String DATABASE_STORAGE_DIRECTORY = "/data/UserActivity/0/com.tma.techday.foodnutrientfact/databases";
    private static final int DATABASE_VERSION = 1;


    public DatabaseCalorieAccess() {
        super(null,DATABASE_NAME, DATABASE_STORAGE_DIRECTORY,null,DATABASE_VERSION);
    }

    @Inject
    public DatabaseCalorieAccess(@Named("ApplicationContext") Context context) {
        super(context, DATABASE_NAME, DATABASE_STORAGE_DIRECTORY, null, DATABASE_VERSION);
    }
}
