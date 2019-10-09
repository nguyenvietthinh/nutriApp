package com.tma.techday.foodnutrientfact.di;

import com.tma.techday.foodnutrientfact.helper.database.CalorieDailyDAO;
import com.tma.techday.foodnutrientfact.helper.database.CalorieSettingDAO;
import com.tma.techday.foodnutrientfact.helper.database.FoodNutriDAO;
import com.tma.techday.foodnutrientfact.helper.database.OrderSharePreferenceDAO;
import com.tma.techday.foodnutrientfact.helper.database.SqlCalorieDailyRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlCalorieSettingRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlFoodNutriRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlOrderRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.SqlUserRepositoryImp;
import com.tma.techday.foodnutrientfact.helper.database.UserDAO;
import com.tma.techday.foodnutrientfact.service.CalorieDailyRepository;
import com.tma.techday.foodnutrientfact.service.CalorieDailyService;
import com.tma.techday.foodnutrientfact.service.CalorieSettingRepository;
import com.tma.techday.foodnutrientfact.service.CalorieSettingService;
import com.tma.techday.foodnutrientfact.service.FoodNutriRepository;
import com.tma.techday.foodnutrientfact.service.FoodNutriService;
import com.tma.techday.foodnutrientfact.service.OrderRepository;
import com.tma.techday.foodnutrientfact.service.OrderService;
import com.tma.techday.foodnutrientfact.service.UserRepository;
import com.tma.techday.foodnutrientfact.service.UserService;

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
    OrderRepository provideOrderRepository (OrderSharePreferenceDAO orderSharePreferenceDAO){
        return new SqlOrderRepositoryImp(orderSharePreferenceDAO);
    }

    @Provides @Singleton
    OrderService provideOrderService(OrderRepository orderRepository){
        return new OrderService(orderRepository);
    }

    @Provides @Singleton
    CalorieDailyRepository provideCalorieDailyRepository (CalorieDailyDAO calorieDailyDAO){
        return new SqlCalorieDailyRepositoryImp(calorieDailyDAO);
    }

    @Provides @Singleton
    CalorieDailyService provideCalorieDailyService(CalorieDailyRepository calorieDailyRepository){
        return new CalorieDailyService(calorieDailyRepository);
    }

    @Provides @Singleton
    UserRepository provideUserRepository (UserDAO userDAO){
        return new SqlUserRepositoryImp(userDAO);
    }

    @Provides @Singleton
    UserService provideUserService(UserRepository userRepository){
        return new UserService(userRepository);
    }

}
