package com.example.demo.controllers;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.beans.OrderItems;
import com.example.demo.beans.Orders;
import com.example.demo.enums.OrderStatus;
import com.example.demo.services.OrderService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/add")
    private ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest){
       //TODO YOU'LL GET THE CUSTOMER INFORMATION FROM AUTHENTIFICATION AFTER
        //TODO ADD LE CHAMP POUR JUSTIFICATIF(Look For best way to store it)

      /*  Account account = new Account();
        account.setDateOuverture(new Date());
        account.setRole(orderRequest.customer.getAccount().getRole());
        account.setLogin(orderRequest.customer.getAccount().getLogin());
        account.setPassword(orderRequest.customer.getAccount().getPassword());
        account.setUserRefID(orderRequest.customer.getAccount().getUserRefID());
        Customer c = new Customer();

        c.setCustomerFirstName(orderRequest.customer.getCustomerFirstName());
        c.setCustomerLastName(orderRequest.customer.getCustomerLastName());
        c.setCustomerType(orderRequest.customer.getCustomerType());
        c.setCustomerPhoneNumber(orderRequest.customer.getCustomerPhoneNumber());
        c.setCustomerAddress(orderRequest.customer.getCustomerAddress());
        c.setAccount(account);*/
       // String customerID = principal.getName();

        System.out.println(orderRequest);
         orderService.createOrders(orderRequest);
        //Orders orders = orderService.createOrders(orderRequest);
        //Orders orders = orderService.createOrders(customerID,orderRequest);
/*
        orders.setOrder_Date(new Date());
        orders.setOrder_Amount(orderRequest.order_Amount);
        orders.setOrder_status(OrderStatus.ATTENTE);
        orders.setCustomer(c);


        orderService.createOrders(orders);*/

     /*
     Signature will be
     public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderrequest, Authentification authentication)
     //   According to chat
        //Retrieve the authenticated customer
        Customer c = (Customer)authentication.getPrincipal();
        orders.setCustomerRef(c);
        //Return response indicating success
        return ResponseEntity.ok().build();
         */

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//Retrieve all orders
    @GetMapping("/lists/all")
    private List<Orders> getOrders(){
        return orderService.getAllOrders();
    }

    //Retrieve order for the specified OrderId
    @GetMapping("/lists/{orderId}")
    private Orders getOrders(@PathVariable("orderId") Integer orderId){
        return orderService.getOrdersById(orderId);
    }

    //Retrieve all orders for the specified customer
 /*   @GetMapping("/lists/{customerID}")//In customer controller🙂
    private List<Orders> getCustomerOrders(@PathVariable("customerID") Integer customerID){
        return orderService.getCustomerOrders(customerID);
    }
   /* @GetMapping("/lists/{customerID}/{orderID}")//In customer controller 🙂
    private Orders getCustomerOrdersById(@PathVariable ("customerID") Integer customerID,@PathVariable("orderID") Integer orderID){
        return orderService.getCustomerOrdersById(customerID,orderID);
    }*/

    public record OrderRequest(
            Date order_Date,
            Double order_Amount,
            OrderStatus order_status,
            Integer deliverRef,
            Integer customerRef,
           // Customer customer
            //
            Integer customerID,
            List<OrderItemRequest> items

    ) {};
    public record OrderItemRequest(
    Integer productId,
    Integer quantity
    ){}
}
