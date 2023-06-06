package com.example.demo.dao;

import com.example.demo.beans.PaymenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentTypeRepository extends JpaRepository<PaymenType,Integer> {
    List<PaymenType> findPaymenTypeByValidityIsTrue();
}
