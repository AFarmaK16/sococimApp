package com.example.demo.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

//    private Double product_price;
    private String product_label;
    private String product_type;
    private String product_description;
    private String product_imageURI;
    private boolean validity = true;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Tarification tarification;

/*
APPARENTLY THEY HAVE MANY PRICE FOR PRODUCTS, AND MANY DESIGNATION TOO
* devise
* */
   /* @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;*/

}
