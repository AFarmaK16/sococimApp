package com.example.demo.controllers;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.beans.OrderItems;
import com.example.demo.beans.Orders;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.services.OrderService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private Orders addOrder(@ModelAttribute("orderRequest") OrderRequest orderRequest,
                                       @RequestParam("justificatif") MultipartFile justificatif,
                                       @RequestParam("items") String items

    ) throws  IOException {
       //TODO YOU'LL GET THE CUSTOMER INFORMATION FROM AUTHENTIFICATION AFTER
        //TODO ADD LE CHAMP POUR JUSTIFICATIF(Look For best way to store it)
//1.CHECK IF IMAGE IS NOT EMPTY
        //2.If file is an image
        //3.If user exist
        //grab some metadata from file if any
        //Store the image



         System.out.println(orderRequest);
         System.out.println("\titems");
//         System.out.println(items);
         System.out.println(justificatif);
         System.out.println(items);
        ObjectMapper mapper = new ObjectMapper();
        List<OrderItemRequest> itemsList = mapper.readValue(items, new TypeReference<List<OrderItemRequest>>() {
        });
        System.out.println(itemsList);
        return  orderService.createOrders(orderRequest, justificatif
                 ,itemsList
         );


//        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Register order Details
//    @PostMapping(value = "/addd")
//    private ResponseEntity<?> addOrderDetails(@RequestParam("orders") Orders orders,@RequestBody List<OrderItemRequest> orderItemRequests
//
//    ) throws IOException {
//
//        System.out.println(orderItemRequests);
//        ObjectMapper mapper = new ObjectMapper();
//        List<OrderItemRequest> itemsList = mapper.readValue((JsonParser) orderItemRequests, new TypeReference<List<OrderItemRequest>>() {
//
//        }) ;
//        System.out.println(itemsList);
//        orderService.setOrderDetails(orders,orderItemRequests);
//
//
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

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
//            List<OrderItemRequest> items,
            Integer customerID
//            @JsonProperty("facture") FactureRequest facture

    ) {};
    public record OrderItemRequest(
    Integer productId,
    Integer quantity
    ){}

    public  record FactureRequest(@JsonProperty("justificatif") MultipartFile justificatif, PaymentStatus paymentStatus){

    }
}
