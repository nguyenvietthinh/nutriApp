package com.thinh.foodnutrientfact.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.thinh.foodnutrientfact.helper.database.DatabaseOpenHelper;
import com.thinh.foodnutrientfact.helper.database.FoodNutriDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @ApplicationContext
    private final Context mContext;


    public DatabaseModule (@ApplicationContext Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelper(){
        return new DatabaseOpenHelper(mContext);
    }

    @Provides
    @Singleton
    FoodNutriDAO provideFoodNutriDAO(){
        return new FoodNutriDAO(provideSQLiteOpenHelper());
    }

}
