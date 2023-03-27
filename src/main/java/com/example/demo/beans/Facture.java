package com.example.demo.beans;

import com.example.demo.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Facture {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facture_id;
    private String justificatif;
//    @Lob
//    private  byte [] payment_justification;
    private Date payment_date = new Date();
    private PaymentStatus paymentStatus;
  /*  @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    private Orders order;*/
}
