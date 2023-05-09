package com.example.demo.services;


import com.example.demo.beans.*;
import com.example.demo.controllers.OrderController;
import com.example.demo.dao.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository ;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private  final ProductRepository productRepository;
    private final FactureRepository factureRepository;
    private final OperatorRepository operatorRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentModeRepository paymentModeRepository;
    private final BankRepository bankRepository;
    private final DestinationRepository destinationRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, ProductRepository productRepository, FactureRepository factureRepository, OperatorRepository operatorRepository, DeliveryRepository deliveryRepository, PaymentModeRepository paymentModeRepository, BankRepository bankRepository, DestinationRepository destinationRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.factureRepository = factureRepository;
        this.operatorRepository = operatorRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentModeRepository = paymentModeRepository;
        this.bankRepository = bankRepository;
        this.destinationRepository = destinationRepository;
    }


    public void updateFacture(String justificatifURI,Integer factureID){
        Facture facture= factureRepository.findById(factureID).get();
        facture.setJustificatifURI(justificatifURI);
        factureRepository.save(facture);
    }
    public Orders createOrders(OrderController.OrderRequest orderRequest, List<OrderController.OrderItemRequest> items, OrderController.FactureRequest factureRequest, OrderController.DeliveryRequest deliveryRequest) throws IOException {
        //Retrieve the customer from database
        Customer customer = customerRepository.findById(orderRequest.customerID()).get();

        Facture facture = new Facture();
        facture.setBank(
                bankRepository.findById(factureRequest.payment_bank()).get());
        facture.setPayment_date(factureRequest.payment_date());
        facture.setPayment_reference(factureRequest.payment_reference());
        //check if that paymentMode exist
        facture.setPaymentMode(paymentModeRepository.findById(factureRequest.payment_mode()).get());
       if (factureRequest.payment_mode().equals("TRANSFERT")){

           Operator operator = operatorRepository.findById(factureRequest.operator()).get();
           facture.setOperator(operator);
       }

        Orders orders = new Orders();
        orders.setCustomer(customer);
        orders.setOrder_status(OrderStatus.valueOf(OrderStatus.ATTENTE.name()));
        orders.setOrder_Amount(orderRequest.order_Amount());
        if (deliveryRequest.isRendu()){
            Delivery delivery = new Delivery();
            delivery.setDelivery_address(deliveryRequest.delivery_address());
            delivery.setDestination(
                  destinationRepository.findById(deliveryRequest.delivery_destination()).get()
            );
            delivery.setDelivery_comment(deliveryRequest.delivery_comment());
            delivery.setRendu(deliveryRequest.isRendu());
            delivery.setDecharged(deliveryRequest.isDecharged());
            deliveryRepository.save(delivery);
            orders.setDelivery(delivery);

        }
        orders.setFacture(facture);
        facture.setPaymentStatus(PaymentStatus.ENCOURS);
        factureRepository.save(facture);
        Orders savedOrder = orderRepository.save(orders);

        for (OrderController.OrderItemRequest item : items) {
            //Retrieve product from database
            Product product = productRepository.findById(item.productId()).get();
            OrderItems orderItems = new OrderItems();
            orderItems.setQuantity(item.quantity());
            orderItems.setProduct(product);
            orderItems.setOrder(orders);
          orderItemRepository.save(orderItems);
            //Add the order item to the lists of order items
          //  items.add(orderItems);

        }
        //Set the order items on the order entity
      //  orders.setOrderItems(items);
        //Know save it to the database
        return savedOrder;
//        return orderRepository.save(orders);
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
    public void updateOrder(Integer id) {
      Orders order = null;
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isPresent())  //test si le code de hashage existe
        {
            order = optionalOrders.get(); //which is in customer type
            order.setOrder_status(OrderStatus.VALIDEE);
            orderRepository.save(order);
        }
    }

    //DELETE ❌ CUSTOMER TODO SEND A NOTIFICATION TO THE ADMIN TO INFORM AN ANNULATION FOR AN ORDER
    public void deleteById(Integer id) {
         orderRepository.deleteById(id);
    }



}
