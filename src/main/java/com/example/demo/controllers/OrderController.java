package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Operator;
import com.example.demo.beans.Orders;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.Payment_Type;
import com.example.demo.services.CustomerService;
import com.example.demo.services.OrderService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }


    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> addOrder(@ModelAttribute("orderRequest") OrderRequest orderRequest, @RequestParam("justificatif") MultipartFile justificatif, @RequestParam("items") String items,@RequestParam("facture") String facture,@RequestParam("delivery") String delivery) throws  IOException {
       //TODO YOU'LL GET THE CUSTOMER INFORMATION FROM AUTHENTIFICATION AFTER
        //TODO ADD LE CHAMP POUR JUSTIFICATIF(Look For best way to store it)
        //1.CHECK IF IMAGE IS NOT EMPTY
        if(justificatif.isEmpty()){
            throw new IllegalStateException("Can't upload emty file ["+justificatif.getSize()+"]");
        }
        //2.If file is an image
//        if (!Arrays.asList("IMAGE_JPEG","IMAGE_PNG","IMAGE_GIF").contains(justificatif.getContentType())){
//            throw new IllegalStateException("FILE MUST BE AN IMAGE");
//        }
        //3.If user exist
        //4.grab some metadata from file if any
        //Store the image



        String uploadDir = System.getProperty("user.dir")+"/justificatifs";
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        Customer customer = customerService.getCustomer(orderRequest.customerID);

        ObjectMapper mapper = new ObjectMapper();
        List<OrderItemRequest> itemsList = mapper.readValue(items, new TypeReference<List<OrderItemRequest>>() {
        });
//        System.out.println(itemsList);
//        Orders savedOrder = orderService.createOrders(orderRequest,itemsList);
        //Mapping Facture String to FactureRequest
        ObjectMapper mapper1 = new ObjectMapper();
        FactureRequest factureRequest = mapper1.readValue(facture, new TypeReference<>() {
        });
        //END MAPPING
        //Mapping Delivery String to DeliveryRequest
        ObjectMapper mapper2 = new ObjectMapper();
        DeliveryRequest deliveryRequest = mapper2.readValue(delivery, new TypeReference<>() {
        });
//        System.out.println(deliveryRequest);
        Orders savedOrder = orderService.createOrders(orderRequest,itemsList,factureRequest,deliveryRequest);
        //END MAPPING
        Integer factureID = savedOrder.getFacture().getFacture_id();
        String fileExtension = justificatif.getOriginalFilename().substring(justificatif.getOriginalFilename().lastIndexOf("."));
        String fileName = customer.getCustomerFirstName()+"_"+customer.getCustomerLastName()+"_"+customer.getCustomerID()+"_order"+savedOrder.getOrder_id()+fileExtension;

        //Save file to the justificatifs directory
        Path path = Paths.get(uploadDir+"/"+fileName);
        Files.copy(justificatif.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        String fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/orders/downloadFile/")
                .path(fileName)
                .toUriString();

        String justificatifURI = fileDownloadURI;
        System.out.println("\t"+justificatifURI);
        orderService.updateFacture(justificatifURI, factureID);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/downloadFile/{filename:.+}")
    private ResponseEntity getJustificatif(@PathVariable String filename, HttpServletRequest request) throws IOException {
        //load file as Resource
        String uploadDir = System.getProperty("user.dir")+"/justificatifs";
        Path path =  Paths.get(uploadDir+"/"+filename);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+resource.getFilename()+"\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

//Retrieve all orders
    @GetMapping("/lists/all")
    private List<Orders> getOrders(){
        return orderService.getAllOrders();
    }

    @PutMapping("/validate/{orderId}")
    private ResponseEntity validateOrder(@PathVariable("orderId") Integer orderId){
         orderService.updateOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //Retrieve order for the specified OrderId
    @GetMapping("/lists/{orderId}")
    private Orders getOrders(@PathVariable("orderId") Integer orderId){
        return orderService.getOrdersById(orderId);
    }


    public record OrderRequest(
            Date order_Date,
            Double order_Amount,
            OrderStatus order_status,
           //TODO dans le input du checkhout ne pas  permettre de choisir une date > aujourd'hui
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
    public  record FactureRequest(
            PaymentStatus paymentStatus,
                                   Date payment_date,
     String payment_reference,
     Integer operator,
     Integer payment_bank,
     Integer payment_mode){

    }
    public record DeliveryRequest(  Integer delivery_ID,
             String delivery_address,
             Integer delivery_destination,
             String delivery_comment,
             boolean isRendu,
             boolean isDecharged){}
}
