package com.example.demo.controllers;

import com.example.demo.beans.Destination;
import com.example.demo.beans.PaymenType;
import com.example.demo.services.DestinationService;
import com.example.demo.services.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderSettings/")
public class OrderSettingsController {
    private final DestinationService destinationService;

    private final PaymentTypeService paymentTypeService;

//@Autowired
//    public OrderSettingsController(DestinationService destinationService, BankService bankService, PaymentTypeService paymentTypeService) {
//        this.destinationService = destinationService;
//        this.paymentTypeService = paymentTypeService;
//    }

    //Destination
    @GetMapping("/destinations")
    public List<Destination> getAllDest(){
        return destinationService.getAllDestinations();
    }

    @PostMapping("/destinations/new")
    public void addDest( @RequestBody DestinationRequest destinationRequest){
        System.out.println(destinationRequest);

        destinationService.addDest(destinationRequest);
//        System.out.println(destinationRequest);
    }
    @PutMapping("/destinations/delete/{destinationID}")
    public  void deleteDestination(@PathVariable("destinationID") Integer id){

        destinationService.delDest(id);
    }
    @PutMapping("/destinations/update/{destinationID}")
    public void updateDestination(@PathVariable("destinationID") Integer id,@RequestBody DestinationRequest destinationRequest){
        System.out.println(destinationRequest);
        destinationService.updateDest(id,destinationRequest);

    }
    ///Banking
//    @GetMapping("/banks")
//    public List<Bank> getBanks(){
//        return bankService.getAllBanks();
//    }
//
//    @PostMapping("/banks/new")
//    public void addBank( @RequestBody BankRequest bankRequest){
//        Bank bank = new Bank();
//        bank.setNom(bankRequest.name);
//        bankService.addBank(bank);
//        System.out.println(bankRequest);
//    }


    //PaymentMode
    @GetMapping("/paymentType")
    public List<PaymenType> getPaymentTypes(){
        return paymentTypeService.getAllPMode();
    }

    @PostMapping("/paymentType/new")
    public void addPaymentType(@RequestBody PaymentTypeRequest paymentTypeRequest){
        PaymenType paymenType = new PaymenType();
        paymenType.setLibelle(paymentTypeRequest.libelle);
        paymentTypeService.addPayType(paymenType);
        System.out.println(paymentTypeRequest);
    }
    @PutMapping("/paymentType/delete/{payTypeID}")
    public  void deletePayType(@PathVariable("payTypeID") Integer id){

        paymentTypeService.delPayType(id);
    }

    public record PaymentTypeRequest(
            String libelle){}
    public record DestinationRequest(
            String city,
            Integer tarification
    ){}
    public record BankRequest(
            String name){}

}
