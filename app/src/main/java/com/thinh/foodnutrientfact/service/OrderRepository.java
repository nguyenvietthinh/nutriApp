package com.thinh.foodnutrientfact.service;

import com.thinh.foodnutrientfact.model.FoodInfoDTO;
import com.thinh.foodnutrientfact.model.Order;

import java.util.List;

public interface OrderRepository {
    boolean addOrderToCard(Order order);
    List<Order> getOrderList();
    void clearCart();
    int getCountCart();
}
