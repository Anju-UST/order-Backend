package com.project.order.service;

import org.springframework.stereotype.Service;

import com.project.order.model.Order;


@Service
public interface OrderService {
    long placeOrder(Order order);

    Order getOrderDetails(long orderId);
    Order updateOrder(Order order);
    void generateBill(Order order);
}