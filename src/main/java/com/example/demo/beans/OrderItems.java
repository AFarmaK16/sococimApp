package com.example.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //private Double price;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Orders order;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Product product;

}
