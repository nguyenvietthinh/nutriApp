package com.tma.techday.foodnutrientfact.di;

import com.tma.techday.foodnutrientfact.helper.database.CalorieSettingDAO;
import com.tma.techday.foodnutrientfact.helper.database.FoodNutriDAO;
import com.tma.techday.foodnutrientfact.helper.database.OrderDAO;
import com.tma.techday.foodnutrientfact.helper.database.SqlCalorieSettingRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlFoodNutriRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlOrderRepositoryImp;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.CalorieSettingRepository;
import com.tma.techday.foodnutrientfact.service.FoodNutriRepository;
import com.tma.techday.foodnutrientfact.service.FoodNutriService;
import com.tma.techday.foodnutrientfact.service.OrderRepository;
import com.tma.techday.foodnutrientfact.service.OrderService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides @Singleton
    FoodNutriRepository provideFoodNutriRepository (FoodNutriDAO foodNutriDAO){
        return new SqlFoodNutriRepositoryImp(foodNutriDAO);
    }

    @Provides @Singleton
    FoodNutriService provideFoodNutriService(FoodNutriRepository foodNutriRepository){
        return new FoodNutriService(foodNutriRepository);
    }

    @Provides @Singleton
    CalorieSettingRepository provideCalorieSettingRepository (CalorieSettingDAO calorieSettingDAO){
        return new SqlCalorieSettingRepositoryImp(calorieSettingDAO);
    }

    @Provides @Singleton
    CalorieSettingService provideCalorieSettingService(CalorieSettingRepository calorieSettingRepository){
        return new CalorieSettingService(calorieSettingRepository);
    }

    @Provides @Singleton
    OrderRepository provideOrderRepository (OrderDAO orderDAO){
        return new SqlOrderRepositoryImp(orderDAO);
    }

    @Provides @Singleton
    OrderService provideOrderService(OrderRepository orderRepository){
        return new OrderService(orderRepository);
    }

}
