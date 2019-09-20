package com.thinh.foodnutrientfact.di;

import com.thinh.foodnutrientfact.helper.database.CalorieSettingDAO;
import com.thinh.foodnutrientfact.helper.database.FoodNutriDAO;
import com.thinh.foodnutrientfact.helper.database.OrderDAO;
import com.thinh.foodnutrientfact.helper.database.SqlCalorieSettingRepositoryImp;
import com.thinh.foodnutrientfact.helper.database.SqlFoodNutriRepositoryImp;
import com.thinh.foodnutrientfact.helper.database.SqlOrderRepositoryImp;
import com.thinh.foodnutrientfact.service.CalorieSettingService;
import com.thinh.foodnutrientfact.service.CalorieSettingRepository;
import com.thinh.foodnutrientfact.service.FoodNutriRepository;
import com.thinh.foodnutrientfact.service.FoodNutriService;
import com.thinh.foodnutrientfact.service.OrderRepository;
import com.thinh.foodnutrientfact.service.OrderService;

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
