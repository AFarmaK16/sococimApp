package com.example.demo.beans;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.Payment_Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter

public class Facture implements Serializable {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facture_id;
//TODO Ajouter un atttribut pour renommer le justificatif sous forme de CUSTOMERNAME_ORDERID_"JUSTIF"

    private String justificatifURI;
    private Date payment_date ;
    private PaymentStatus paymentStatus;
    private String payment_reference;

    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Operator operator;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Bank bank;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private PaymentMode paymentMode;


}

