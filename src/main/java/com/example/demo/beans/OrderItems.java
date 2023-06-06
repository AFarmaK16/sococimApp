package com.example.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class OrderItems implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //private Double price;
    private Double quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Orders order;
    //TODO REVOIR LA LIAISON ORDERITEM ETPRODUCT, METTRE ATTRIBUTS RATHER THAN RELATION DE COMPOSITION OU AGGREG POUR QUE LA SUPPRESSION D'UN PRODUIT N;ENTRAINNE PAS LA SUPPRESSION D'UN PRODUIT
    //COMMANDEE HISTOIRE DE GARDER L'HISTORIQUE
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Product product;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Tarification tarification;

}
