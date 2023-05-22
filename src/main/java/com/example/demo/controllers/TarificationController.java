package com.example.demo.controllers;

import com.example.demo.beans.Orders;
import com.example.demo.beans.Tarification;
import com.example.demo.services.TarificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/tarifs/")
public class TarificationController {
    private final TarificationService tarificationService;
@Autowired
    public TarificationController(TarificationService tarificationService) {
        this.tarificationService = tarificationService;
    }

    @GetMapping("/list/all")
    public List<Tarification> getTarifications(){
        return tarificationService.getAllTarifications();
    }

    @GetMapping("/list/{tarifID}")
    private Tarification getCustomerOrdersById(@PathVariable("tarifID") Integer orderID){
        return tarificationService.getTarificationByID(orderID);
    }
    @GetMapping("/new")
    public void addTarification( @RequestBody TarificationRequest tarificationRequest){
    Tarification tarification = new Tarification();
        tarification.setMontant(tarificationRequest.montant);
        tarification.setDatefin(tarificationRequest.datefin);
         tarificationService.addTarification(tarification);
         System.out.println(tarificationRequest);
    }
    public record TarificationRequest(
             Date datefin,
             Integer montant
            ){}
}
