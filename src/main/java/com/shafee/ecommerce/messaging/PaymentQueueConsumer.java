
package com.shafee.ecommerce.messaging;

import com.shafee.ecommerce.eventHandler.PaymentEventHandler;
import com.shafee.ecommerce.model.Order;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueConsumer {
    private final PaymentEventHandler paymentEventHandler;

    public PaymentQueueConsumer(PaymentEventHandler paymentEventHandler) {
        this.paymentEventHandler = paymentEventHandler;
    }

    public void processOrder(Order order) {
        paymentEventHandler.processPayment(order);
    }
}