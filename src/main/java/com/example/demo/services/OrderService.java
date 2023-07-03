package com.example.demo.services;


import com.example.demo.beans.*;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.OrderController.*;
import com.example.demo.dao.*;
import com.example.demo.dao.PaymentModesRepository;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository ;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private  final ProductRepository productRepository;
    private final FactureRepository factureRepository;
    private final PaymentModesRepository paymentModesRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DestinationRepository destinationRepository;

//    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, ProductRepository productRepository, FactureRepository factureRepository, PaymentModesRepository paymentModesRepository, DeliveryRepository deliveryRepository, PaymentTypeRepository paymentTypeRepository, BankRepository bankRepository, DestinationRepository destinationRepository) {
//        this.orderRepository = orderRepository;
//        this.orderItemRepository = orderItemRepository;
//        this.customerRepository = customerRepository;
//        this.productRepository = productRepository;
//        this.factureRepository = factureRepository;
//        this.paymentModesRepository = paymentModesRepository;
//        this.deliveryRepository = deliveryRepository;
//        this.paymentTypeRepository = paymentTypeRepository;
//        this.bankRepository = bankRepository;
//        this.destinationRepository = destinationRepository;
//    }


    public void updateFacture(List<String> justificatifURI,Integer factureID){
        Facture facture= factureRepository.findById(factureID).get();
        facture.setJustificatifURIs(justificatifURI);
        factureRepository.save(facture);
    }
    public Orders createOrders(OrderRequest orderRequest, List<OrderItemRequest> items, FactureRequest factureRequest, DeliveryRequest deliveryRequest, Customer customer) throws IOException {
        //Retrieve the customer from database
//        Customer customer = customerRepository.findById(orderRequest.customerID()).get();

        Facture facture = new Facture();
//        facture.setBank(
//                bankRepository.findById(factureRequest.payment_bank()).get());
        facture.setPayment_date(factureRequest.payment_date());
        facture.setPayment_reference(factureRequest.payment_reference());
        //check if that paymentMode exist
        facture.setPaymenType(paymentTypeRepository.findById(factureRequest.payment_mode()).get());
//       if (factureRequest.payment_mode().equals("TRANSFERT")){

           com.example.demo.beans.PaymentModes paymentModes = this.paymentModesRepository.findById(factureRequest.operator()).get();
           facture.setPaymentModes(paymentModes);
//       }

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

        for (OrderItemRequest item : items) {
            //Retrieve product from database
            Product product = productRepository.findById(item.productId()).get();
            OrderItems orderItems = new OrderItems();
            orderItems.setQuantity(item.quantity());
            orderItems.setProduct(product);
            orderItems.setOrder(orders);
            orderItems.setTarification(product.getTarification());
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

    public int calculateTotal( List<OrderItemRequest> items,DeliveryRequest deliveryRequest ,boolean isDecharged) throws IOException {

int total =0;
int tht =0;
double tva = 0.18;
int redevance_habitat = 2000; //totalquantity * 2000
int taxe_consommation =  3000; //3000 * totalquantity
double totalQuantity =0;
int deliveryPrice =0;
int totalTva = 0;
int dechargement=0;

        for (OrderItemRequest item : items) {
            //Retrieve product from database
            Product product = productRepository.findById(item.productId()).get();
            tht +=(product.getTarification().getMontant()* item.quantity());
            totalQuantity+=item.quantity();

        }
        if (deliveryRequest.isRendu()){
            deliveryPrice= (int) (destinationRepository.findById(deliveryRequest.delivery_destination()).get().getTarification().getMontant()* totalQuantity);
        }
        if (isDecharged){
            dechargement = (int) (totalQuantity * 400);
        }

        totalTva = (int) ((tht* tva)+(deliveryPrice * tva)+(dechargement * tva)+ redevance_habitat * totalQuantity * tva + (taxe_consommation * totalQuantity * tva));

    total= (int) ((redevance_habitat* totalQuantity) + (taxe_consommation * totalQuantity) + tht + totalTva +dechargement +deliveryPrice);
    System.out.println("LA TOTALE EST "+total);
        return total;

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
    public Orders updateOrder(Integer id) {
      Orders order = null;
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isPresent())  //test si le code de hashage existe
        {
            order = optionalOrders.get(); //which is in customer type
            order.setOrder_status(OrderStatus.VALIDEE);
            orderRepository.save(order);

        }
        return  order;
    }
    //MARK ORDER AS DELIVERED
    public Orders updateOrderState(Integer id, SetDeliveryRequest setDeliveryRequest) {
        Orders order = null;
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isPresent())  //test si le code de hashage existe
        {
            order = optionalOrders.get(); //which is in customer type
            order.getDelivery().setDriver(setDeliveryRequest.driver());
            order.getDelivery().setTruckIM(setDeliveryRequest.truckIM());
            order.getDelivery().setDeliverDate(setDeliveryRequest.deliverDate());
            order.setOrder_status(OrderStatus.EN_ATTENTE_DE_LIVRAISON);
            orderRepository.save(order);

        }
        return  order;
    }
    //DELETE ❌ CUSTOMER TODO SEND A NOTIFICATION TO THE ADMIN TO INFORM AN ANNULATION FOR AN ORDER
    public void deleteById(Integer id) {
         orderRepository.deleteById(id);
    }



}
