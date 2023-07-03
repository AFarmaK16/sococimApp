package com.example.demo.controllers;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.beans.Orders;
import com.example.demo.dao.AccountRepository;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.services.CustomerService;
import com.example.demo.services.EmailService;
import com.example.demo.services.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.constant.EmailConstant.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final AccountRepository accountRepository;

//    @Autowired
//    public OrderController(OrderService orderService, CustomerService customerService) {
//        this.orderService = orderService;
//        this.customerService = customerService;
//    }
@PostMapping("/calculateTotal")
private Integer calculateTotal( @RequestParam("items") String items, @Nullable @RequestParam("delivery") String delivery,@Nullable @RequestParam("isDecharged") Boolean isDecharged) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<OrderItemRequest> itemsList = mapper.readValue(items, new TypeReference<List<OrderItemRequest>>() {});
    ObjectMapper mapper2 = new ObjectMapper();
    DeliveryRequest deliveryRequest = mapper2.readValue(delivery, new TypeReference<>() { });
    int total = orderService.calculateTotal(itemsList, deliveryRequest,isDecharged);
    System.out.println(items);
    System.out.println(delivery);
    System.out.println(isDecharged);
    return total;

}

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> addOrder(@ModelAttribute("orderRequest") OrderRequest orderRequest,
//                                       @RequestParam("justificatifList") MultipartFile justificatifList,
                                       @RequestParam("justificatif") List<MultipartFile> justificatifList,
                                       @RequestParam("items") String items,
                                       @RequestParam("facture") String facture,
                                       @RequestParam("delivery") String delivery) throws IOException, MessagingException {

        //1.CHECK IF IMAGE IS NOT EMPTY

        System.out.println(justificatifList);
        System.out.println(justificatifList.size());
        if(justificatifList.isEmpty()){
            throw new IllegalStateException("Can't upload emty file ");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Customer customer = account.getCustomer();
        // Retrieve the authenticated user's information, including the customerId
        String loggedUserName = authentication.getName(); // Assuming customerId is stored as the username
    System.out.println("The current logged user is "+loggedUserName);


        //3.If user exist
        //4.grab some metadata from file if any
        //Creating justificatif directory if not exist


        String uploadDir = System.getProperty("user.dir")+"/justificatifs";
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdir();
        }
//        Customer customer = customerService.getCustomer(orderRequest.customerID);

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
        Orders savedOrder = orderService.createOrders(orderRequest,itemsList,factureRequest,deliveryRequest,customer);
        //END MAPPING
        Integer factureID = savedOrder.getFacture().getId();
        List<String> justificatifURIs = new ArrayList<>();;
        for (int i = 0; i < justificatifList.size(); i++) {
            MultipartFile justificatif = justificatifList.get(i);

            // 2. Check if the file is an image (optional)
            if (!justificatif.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image");
            }
            // 3. Check if size doesn't exceed 1Mo
            if (justificatif.getSize() > 1024 * 1024) {
                // File size exceeds the limit
                throw new IllegalArgumentException("File size exceeds the limit");
            }
            //Storing images
            String fileExtension = justificatif.getOriginalFilename().substring(justificatif.getOriginalFilename().lastIndexOf("."));
            String fileName = customer.getName()+"_"+customer.getSurname()+"_"+customer.getCustomerID()+"_order"+savedOrder.getOrder_id()+"("+i+")"+fileExtension;
//Save file to the justificatifs directory
            Path path = Paths.get(uploadDir+"/"+fileName);
            Files.copy(justificatif.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/orders/downloadFile/")
                    .path(fileName)
                    .toUriString();
            String justificatifURI = fileDownloadURI;
            System.out.println("simple >>>\t"+justificatifURI);
            justificatifURIs.add(justificatifURI);


        }
        System.out.println(justificatifURIs);
                    orderService.updateFacture(justificatifURIs, factureID);
String order_email = PENDING_ORDER_EMAIL+ emailService.createOrderEmailText(savedOrder.getOrder_id());
        emailService.sendEmail(customer.getName(),account.getUsername(),order_email,PENDING_ORDER_EMAIL_SUBJECT);





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
    private ResponseEntity validateOrder(@PathVariable("orderId") Integer orderId) throws MessagingException {
         Orders order = orderService.updateOrder(orderId);
         Customer customer = order.getCustomer();
         Optional<Account> optionalAccount = accountRepository.findAccountByCustomer_CustomerID(customer.getCustomerID());

        String order_email = PENDING_FOR_DELIVERY_ORDER_EMAIL+ emailService.createOrderEmailText(orderId);
        emailService.sendEmail(order.getCustomer().getName(),optionalAccount.get().getUsername(),order_email,PENDING_FOR_DELIVERY_ORDER_EMAIL_SUBJECT);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/changeState/{orderId}")
    private ResponseEntity deliverOrder(@PathVariable("orderId") Integer orderId,@RequestBody SetDeliveryRequest setDeliveryRequest) throws MessagingException {
       System.out.println(orderId+">>>>\t"+setDeliveryRequest);
    Orders order =     orderService.updateOrderState(orderId,setDeliveryRequest);
        Customer customer = order.getCustomer();
        Optional<Account> optionalAccount = accountRepository.findAccountByCustomer_CustomerID(customer.getCustomerID());
        String order_email = "Votre commande a bien été validée et vous sera livrée le "+setDeliveryRequest.deliverDate+
                "\nVeuillez trouver ci-aprés les details de la livraison:\n"+
                        "\n\t Immatriculation Camion :"+setDeliveryRequest.truckIM+
                        "\n \tConducteur :"+setDeliveryRequest.driver+
                        "\n \tDate de livraison :"+setDeliveryRequest.deliverDate
                + emailService.createOrderEmailText(orderId);
        emailService.sendEmail(order.getCustomer().getName(),optionalAccount.get().getUsername(),order_email,DELIVERY_ORDER_EMAIL_SUBJECT);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @PutMapping("/changeState/{orderId}")
//    private ResponseEntity deliverOrder(@PathVariable("orderId") Integer orderId){
//        orderService.updateOrderState(orderId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//    Retrieve order for the specified OrderId
    @GetMapping("/lists/{orderId}")
    private Orders getOrders(@PathVariable("orderId") Integer orderId){
        return orderService.getOrdersById(orderId);
    }


    public record OrderRequest(
            Date order_Date,
            Double order_Amount,
            OrderStatus order_status,
           // Customer customer
            //
//            List<OrderItemRequest> items,
            Integer customerID
//            @JsonProperty("facture") FactureRequest facture

    ) {};
    public record OrderItemRequest(
    Integer productId,
    Double quantity
    ){}
    public  record FactureRequest(
            PaymentStatus paymentStatus,
                                   Date payment_date,
     String payment_reference,
     Integer operator,
     Integer payment_mode){

    }
    public record DeliveryRequest(  Integer delivery_ID,
             String delivery_address,
             Integer delivery_destination,
             String delivery_comment,
             boolean isRendu,
             boolean isDecharged){}
    public record SetDeliveryRequest(
                                       String truckIM,
                                       String driver,
                                       Date deliverDate)
    {
    }
}

