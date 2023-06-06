package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Orders;
import com.example.demo.services.CustomerService;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/customers/")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;


    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    //Retrieve all orders for the specified customer
    @GetMapping("/{customerID}/orders/all")//In customer controller🙂
    private List<Orders> getCustomerOrders(@PathVariable("customerID") Integer customerID){
        return orderService.getCustomerOrders(customerID);
    }
    //Retrieve order with "orderId"for the specified customer
    @GetMapping("/{customerID}/orders/{orderID}")
    private Orders getCustomerOrdersById(@PathVariable ("customerID") Integer customerID,@PathVariable("orderID") Integer orderID){
        return orderService.getCustomerOrdersById(customerID,orderID);
    }
    /*public ClientController(List<Customer> customers) {
        this.customers = customers;
    }*/
    @GetMapping("/add")
    public void addCustomer(@RequestBody CustomerRequest customerRequest){
//    Account account = new Account();
//    account.setDateOuverture(new Date());
//    account.setRole(customerRequest.account.getRole());
//    account.setUsername(customerRequest.account.getUsername());
//    account.setPassword(customerRequest.account.getPassword());
    Customer c = new Customer();

        c.setName(customerRequest.customerFirstName);
        c.setSurname(customerRequest.customerLastName);
//        c.setCustomerType(CustomerType.COMPTANT);
        c.setPhoneNumber(customerRequest.customerPhoneNumber);
        c.setAddress(customerRequest.customerAddress);
//        c.setAccount(account);

        customerService.createCustomer(c);
    }
    @GetMapping("/lists")
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();

    }
    @GetMapping("{customerId}")
    public  Customer getCustomer(@PathVariable("customerId") Integer id){
        return customerService.getCustomer(id);
    }
  @DeleteMapping("/delete/{customerId}")//❌
    public  void deleteCustomer(@PathVariable("customerId") Integer id){

         customerService.deleteCustomer(id);
    }


    record CustomerRequest(
             String customerFirstName,
             String customerLastName,
             String customerAddress,
//             CustomerType customerType,
             String customerPhoneNumber
//            ,
//             Account account
                          ) {

    }
}
