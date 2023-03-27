package com.example.demo.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private Double product_price;
    private String product_label;
    private String product_type;
    //TODO GAMME DE PRODUIT  OU TYPE
    private String product_description;
    private String product_image;
    
/*
APPARENTLY THEY HAVE MANY ORICE FOR PRODUCTS, AND MANY DESIGNATION TOO
* devise
* */
   /* @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;*/

}
