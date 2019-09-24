package com.tma.techday.foodnutrientfact.helper.database;

import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.service.OrderRepository;

import java.util.List;

import javax.inject.Inject;

public class SqlOrderRepositoryImp implements OrderRepository {

    private OrderDAO orderDAO;

    @Inject
    public SqlOrderRepositoryImp(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public boolean addOrderToCard(Order order) {
        return orderDAO.addOrderToCard(order);
    }

    @Override
    public List<Order> getOrderList() {
        return orderDAO.getOrderList();
    }

    @Override
    public void clearCart() {
        orderDAO.clearCart();
    }

    @Override
    public int getCountCart() {
        return orderDAO.getCountCart();
    }

}
