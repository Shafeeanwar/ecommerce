package com.shafee.ecommerce.messaging;

import com.shafee.ecommerce.model.Order;
import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;

@Service
public class MessageQueueService {
    private final BlockingQueue<Order> orderQueue;
    private final BlockingQueue<Order> paymentQueue;
    private final OrderQueueConsumer orderConsumer;
    private final PaymentQueueConsumer paymentConsumer;

    public MessageQueueService(OrderQueueConsumer orderConsumer, PaymentQueueConsumer paymentConsumer) {
        this.orderQueue = new LinkedBlockingQueue<>();
        this.paymentQueue = new LinkedBlockingQueue<>();
        this.orderConsumer = orderConsumer;
        this.paymentConsumer = paymentConsumer;
    }

    @PostConstruct
    public void startConsumers() {
        // Start order consumer thread
        new Thread(() -> {
            while (true) {
                try {
                    Order order = orderQueue.take();
                    orderConsumer.processOrder(order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "order-consumer").start();

        // Start payment consumer thread
        new Thread(() -> {
            while (true) {
                try {
                    Order order = paymentQueue.take();
                    paymentConsumer.processOrder(order);
                    sendPaymentStatusEvent(order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "payment-consumer").start();
    }

    public void sendNewOrderEvent(Order order) {
        try {
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to queue order", e);
        }
    }

    public void sendPaymentStatusEvent(Order order) {
        try {
            paymentQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to queue payment", e);
        }
    }
}