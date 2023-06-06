package com.example.demo.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;
    private String name;
    private String surname;
//    private CustomerType customerType;
    private String address;
    private String phoneNumber;
//    @OneToOne(cascade = CascadeType.ALL)
//    private Account account;
  /*  @OneToMany(mappedBy = "client")
    private List<Orders> ordersList;*/


}
