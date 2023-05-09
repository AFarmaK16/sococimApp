package com.example.demo.dao;

import com.example.demo.beans.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository  extends JpaRepository<Delivery,Integer> {
}
