package com.example.demo.dao;

import com.example.demo.beans.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentModeRepository extends JpaRepository<PaymentMode,Integer> {
}
