package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.service.OrderRepository;

import java.util.List;

import javax.inject.Inject;

public class SqlOrderRepositoryImp implements OrderRepository {

    private OrderSharePreferenceDAO orderSharePreferenceDAO;


    @Inject
    public SqlOrderRepositoryImp(OrderSharePreferenceDAO orderSharePreferenceDAO) {
        this.orderSharePreferenceDAO = orderSharePreferenceDAO;
    }

    @Override
    public boolean addOrderToCard(Order order) {
        return orderSharePreferenceDAO.addOrderToCard(order);
    }

    @Override
    public List<Order> getOrderList() {
        return orderSharePreferenceDAO.getCart();
    }

    @Override
    public void clearCart() {
        orderSharePreferenceDAO.clearCart();
    }

    @Override
    public int getCountCart() {
        return orderSharePreferenceDAO.getCountCart();
    }

}
