package com.example.demo.dao;

import com.example.demo.beans.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems,Integer> {
}
