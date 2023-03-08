package com.example.demo.services;


import com.example.demo.beans.Customer;
import com.example.demo.beans.OrderItems;
import com.example.demo.beans.Orders;
import com.example.demo.beans.Product;
import com.example.demo.controllers.OrderController;
import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.OrderItemRepository;
import com.example.demo.dao.OrderRepository;
import com.example.demo.dao.ProductRepository;
import com.example.demo.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository ;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private  final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    //CREATE DONE😋❌CUSTOMER/ADMIN
   /* public Orders createOrders(Orders order) {
        List<OrderItems> items = order.getOrderItems();
        order.setOrderItems(null);
        order = orderRepository.save(order);
        for (OrderItems item : items) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }
        order.setOrderItems(items);
        return order;
    }*/
    public void createOrders(OrderController.OrderRequest orderRequest) {
        //Retrieve the customer from database
        Customer customer = customerRepository.findById(orderRequest.customerID()).get();
      Orders orders = new Orders();
      orders.setCustomer(customer);
      orders.setOrder_status(OrderStatus.valueOf(OrderStatus.ATTENTE.name()));
      orders.setOrder_Amount(orderRequest.order_Amount());
        List<OrderItems> items = new ArrayList<>();
        orderRepository.save(orders);

        for (OrderController.OrderItemRequest item : orderRequest.items()) {
            //Retrieve product from database
            Product product = productRepository.findById(item.productId()).get();
            OrderItems orderItems = new OrderItems();
            orderItems.setQuantity(item.quantity());
            orderItems.setProduct(product);
            orderItems.setOrder(orders);
            orderItemRepository.save(orderItems);
            //Add the order item to the lists of order items
            items.add(orderItems);

        }
        //Set the order items on the order entity
      //  orders.setOrderItems(items);
        //Know save it to the database

        //return orderRepository.save(orders);
    }
    //READ [ALL] DONE😋❌ ADMIN
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }
    //READ [ONE] DONE😋❌ ADMIN/CUSTOMER
    public Orders getOrdersById(Integer id) {
        return orderRepository.findById(id).get();
    }
    //READ [ALL ORDER FOR A SPECIFIC CUSTOMER]  DONE😋 ❌ ADMIN/CUSTOMER
    public List<Orders> getCustomerOrders(Integer customerID) {
        return orderRepository.findOrdersByCustomerCustomerID(customerID);
    }
    //READ [ONE FOR A SPECIFIC CUSTOMER]  DONE😋❌ ADMIN/CUSTOMER
   public Orders getCustomerOrdersById(Integer customerID,Integer orderID) {
        return orderRepository.findOrdersByCustomerCustomerIDAndOrder_id(customerID,orderID);
    }
    //UPDATE
    public void updateOrder(Orders order) {
         orderRepository.save(order);
    }

    //DELETE ❌ CUSTOMER TODO SEND A NOTIFICATION TO THE ADMIN TO INFORM AN ANNULATION FOR AN ORDER
    public void deleteById(Integer id) {
         orderRepository.deleteById(id);
    }



}
