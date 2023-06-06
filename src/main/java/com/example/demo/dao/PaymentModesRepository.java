package com.example.demo.dao;

import com.example.demo.beans.PaymentModes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentModesRepository extends JpaRepository<com.example.demo.beans.PaymentModes,Integer> {
    List<PaymentModes> findPaymentModesByValidityTrue();
}
