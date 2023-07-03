package com.example.demo.beans;

import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter

public class Facture implements Serializable {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//TODO Ajouter un atttribut pour renommer le justificatif sous forme de CUSTOMERNAME_ORDERID_"JUSTIF"

    @ElementCollection
    private  List<String> justificatifURIs;

//    private String justificatifURI;
    private Date payment_date ;
    private PaymentStatus paymentStatus;
    private String payment_reference;

    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private PaymentModes paymentModes;
//    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    private Bank bank;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private PaymenType paymenType;


}

