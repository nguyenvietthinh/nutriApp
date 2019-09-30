package com.tma.techday.foodnutrientfact.gui.event;

import com.tma.techday.foodnutrientfact.model.Order;

public class DeleteOrderEvent {
    private Order orderDeleted;

    public DeleteOrderEvent(Order orderDeleted) {
        this.orderDeleted = orderDeleted;
    }

    public Order getOrderDeleted() {
        return orderDeleted;
    }

    public void setOrderDeleted(Order orderDeleted) {
        this.orderDeleted = orderDeleted;
    }
}
