package com.shafee.ecommerce.eventHandler;

import com.shafee.ecommerce.model.Order;

public interface PaymentEventInterface {

    public void processPayment(Order order);

    public void rollbackPayment(Order order);
}
