package com.example.demo.beans;

import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    private Date order_Date= new Date();
    private Double order_Amount;
    private OrderStatus order_status;
    private Integer deliverRef;
   private Integer customerRef;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

   @OneToMany(mappedBy = "order",cascade =CascadeType.ALL)
    private List<OrderItems> orderItems;



}
