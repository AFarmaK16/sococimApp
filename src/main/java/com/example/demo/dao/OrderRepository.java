package com.example.demo.dao;
import com.example.demo.beans.Customer;
import com.example.demo.beans.Orders;
import com.example.demo.controllers.OrderController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findOrdersByCustomerCustomerID(Integer customerID);
    //List<Orders> findByCustomer(Integer customerID);
    //Orders getOrdersByOrder_idAndCustomerCustomerID(Integer orderID,Integer customerID);
    //findOrdersByOrder_idAndCustomerCustomerID
    @Query("SELECT o from Orders o JOIN o.customer c WHERE c.customerID = :customerID and  o.order_id = :orderID")
    Orders findOrdersByCustomerCustomerIDAndOrder_id(@Param("customerID") Integer customerID, @Param("orderID") Integer orderID);
   // Orders findOrdersByCustomerCustomerIDAndOrder_id(Integer customerID,Integer orderID);
    //Orders findOrdersByCustomerAndOrder_id (Integer customerID,Integer orderID);
    //Orders createOrders(OrderController.OrderRequest orderRequest);
}
