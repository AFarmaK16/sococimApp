package com.example.demo.beans;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.Payment_Type;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payment_id;
    private String payment_reference;
    private byte[] justificatif;
    private Double order_amount;
    private Payment_Type payment_type;
    private Date payment_date = new Date();
    private PaymentStatus paymentStatus;
}
