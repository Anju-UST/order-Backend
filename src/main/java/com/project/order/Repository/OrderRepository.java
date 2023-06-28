package com.project.order.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.order.model.Order;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
    
}