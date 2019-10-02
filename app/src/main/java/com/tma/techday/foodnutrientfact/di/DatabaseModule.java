package com.tma.techday.foodnutrientfact.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.tma.techday.foodnutrientfact.helper.database.CalorieDailyDAO;
import com.tma.techday.foodnutrientfact.helper.database.CalorieSettingDAO;
import com.tma.techday.foodnutrientfact.helper.database.DatabaseCalorieAccess;
import com.tma.techday.foodnutrientfact.helper.database.DatabaseOpenHelper;
import com.tma.techday.foodnutrientfact.helper.database.FoodNutriDAO;
import com.tma.techday.foodnutrientfact.helper.database.OrderSharePreferenceDAO;

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

    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelperCalorieDb(){
        return new DatabaseCalorieAccess(mContext);
    }

    @Provides
    @Singleton
    CalorieSettingDAO provideCalorieDAO(){
        return new CalorieSettingDAO(provideSQLiteOpenHelperCalorieDb());
    }



    @Provides
    @Singleton
    OrderSharePreferenceDAO provideOrderSharePreferenceDAO(){
        return new OrderSharePreferenceDAO(mContext);
    }

    @Provides
    @Singleton
    CalorieDailyDAO provideCalorieDailyDAO(){
        return new CalorieDailyDAO(provideSQLiteOpenHelperCalorieDb());
    }



}
