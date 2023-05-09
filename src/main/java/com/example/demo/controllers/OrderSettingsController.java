package com.example.demo.controllers;

import com.example.demo.beans.Bank;
import com.example.demo.beans.Destination;
import com.example.demo.beans.PaymentMode;
import com.example.demo.services.BankService;
import com.example.demo.services.DestinationService;
import com.example.demo.services.PaymentModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
//@AllArgsConstructor
@RequestMapping("/api/v1/orderSettings/")
public class OrderSettingsController {
    private final DestinationService destinationService;
    private final BankService bankService;
    private final PaymentModeService paymentModeService;
@Autowired
    public OrderSettingsController(DestinationService destinationService, BankService bankService, PaymentModeService paymentModeService) {
        this.destinationService = destinationService;
        this.bankService = bankService;
        this.paymentModeService = paymentModeService;
    }

    //Destination
    @GetMapping("/destinations")
    public List<Destination> getAllDest(){
        return destinationService.getAllDestinations();
    }

    @PostMapping("/destinations/new")
    public void addDest( @RequestBody DestinationRequest destinationRequest){
        System.out.println(destinationRequest);
        Destination destination = new Destination();
        destination.setNom(destinationRequest.city);
        destination.setTarif(destinationRequest.tarif);
        destinationService.addDest(destination);
//        System.out.println(destinationRequest);
    }

    ///Banking
    @GetMapping("/banks")
    public List<Bank> getBanks(){
        return bankService.getAllBanks();
    }

    @PostMapping("/banks/new")
    public void addBank( @RequestBody BankRequest bankRequest){
        Bank bank = new Bank();
        bank.setNom(bankRequest.name);
        bankService.addBank(bank);
        System.out.println(bankRequest);
    }


    //PaymentMode
    @GetMapping("/paymentModes")
    public List<PaymentMode> getPaymentModes(){
        return paymentModeService.getAllPMode();
    }

    @PostMapping("/paymentModes/new")
    public void addPaymentMode(@RequestBody PaymentModeRequest paymentModeRequest){
        PaymentMode paymentMode = new PaymentMode();
        paymentMode.setLibelle(paymentModeRequest.libelle);
        paymentModeService.addPMode(paymentMode);
        System.out.println(paymentModeRequest);
    }
    public record PaymentModeRequest(
            String libelle){}
    public record DestinationRequest(
            String city,
            Double tarif
    ){}
    public record BankRequest(
            String name){}

}
