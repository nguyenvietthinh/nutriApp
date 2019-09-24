package com.tma.techday.foodnutrientfact.service;

import com.tma.techday.foodnutrientfact.model.Order;

import java.util.List;

public interface OrderRepository {
    boolean addOrderToCard(Order order);
    List<Order> getOrderList();
    void clearCart();
    int getCountCart();
}
