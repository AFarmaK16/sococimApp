package com.example.demo.beans;

import com.example.demo.enums.CustomerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;
    private String customerFirstName;
    private String customerLastName;
//    private CustomerType customerType;
    private String customerAddress;
    private String customerPhoneNumber;
//    @OneToOne(cascade = CascadeType.ALL)
//    private Account account;
  /*  @OneToMany(mappedBy = "client")
    private List<Orders> ordersList;*/


}
