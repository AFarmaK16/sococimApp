package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Orders;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.services.CustomerService;
import com.example.demo.services.OrderService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.ContentType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.util.ContentTypeUtils;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
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
    private ResponseEntity<?> addOrder(@ModelAttribute("orderRequest") OrderRequest orderRequest, @RequestParam("justificatif") MultipartFile justificatif, @RequestParam("items") String items) throws  IOException {
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



        //        String fileName = StringUtils.cleanPath(justificatif.getOriginalFilename());


         System.out.println(orderRequest);
         System.out.println("\titems");
//         System.out.println(items);
         System.out.println(justificatif);
         System.out.println(items);
        ObjectMapper mapper = new ObjectMapper();
        List<OrderItemRequest> itemsList = mapper.readValue(items, new TypeReference<List<OrderItemRequest>>() {
        });
        System.out.println(itemsList);
        Orders savedOrder = orderService.createOrders(orderRequest, justificatif
                ,itemsList
        );
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
//
//        System.out.println("order"+ orderService.createOrders(orderRequest, justificatif
//                ,itemsList
//        ).getOrder_id());
//        return  orderService.createOrders(orderRequest, justificatif
//                 ,itemsList
//         );


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/downloadFile/{filename:.+}")
    private ResponseEntity getJustificatif(@PathVariable String filename, HttpServletRequest request) throws IOException {
        //load file as Resource
        String uploadDir = System.getProperty("user.dir")+"/justificatifs";
        Path path =  Paths.get(uploadDir+"/"+filename);
//        FileStore fileStore = Files.getFileStore(Path.of(uploadDir +"/"+ filename));
////        File file = new File(filename);
////        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
////        InputStreamResource resource = FileStore.class.loadFileAsRessource(filename);
Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+resource.getFilename()+"\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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
