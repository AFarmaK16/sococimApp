package com.example.demo.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.context.annotation.Bean;

@Entity
public class Facture {

@Id
@GeneratedValue
    private String facture_id;
    private String justificatif;

}
