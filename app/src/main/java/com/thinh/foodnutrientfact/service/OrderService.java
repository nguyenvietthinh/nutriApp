package com.thinh.foodnutrientfact.service;

import com.thinh.foodnutrientfact.model.Order;

import java.util.List;

import javax.inject.Inject;

public class OrderService {
    private OrderRepository orderRepository;

    @Inject
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean addOrderToCard(Order order){
        return orderRepository.addOrderToCard(order);
    }

    public List<Order> getOrderList(){
        return orderRepository.getOrderList();
    }

    public void clearCart(){
         orderRepository.clearCart();
    }

    public int getCountCart(){
        return orderRepository.getCountCart();
    }
}
