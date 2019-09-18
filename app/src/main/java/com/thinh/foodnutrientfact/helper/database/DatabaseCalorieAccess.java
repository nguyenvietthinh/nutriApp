package com.thinh.foodnutrientfact.helper.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import javax.inject.Inject;
import javax.inject.Named;

public class DatabaseCalorieAccess extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "nutri_app_db.db";
    private static final String DATABASE_STORAGE_DIRECTORY = "/data/user/0/com.thinh.filebaseimagelabeling/databases";
    private static final int DATABASE_VERSION = 1;


    public DatabaseCalorieAccess() {
        super(null,DATABASE_NAME, DATABASE_STORAGE_DIRECTORY,null,DATABASE_VERSION);
    }

    @Inject
    public DatabaseCalorieAccess(@Named("ApplicationContext") Context context) {
        super(context, DATABASE_NAME, DATABASE_STORAGE_DIRECTORY, null, DATABASE_VERSION);
    }
}
