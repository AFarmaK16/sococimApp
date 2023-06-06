package com.example.demo.controllers;

import com.example.demo.beans.PaymentModes;
import com.example.demo.enums.PaymentMode;
import com.example.demo.services.PaymentModesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PaymentModeController {
    private final PaymentModesService paymentModesService;
//@Autowired
//    public PaymentModeController(OperatorService paymentModesService) {
//        this.paymentModesService = paymentModesService;
//    }

    @GetMapping("/payModes")
    public List<PaymentModes> getPayModes(){
        return paymentModesService.getAllPaymentModess();
    }

    @PostMapping("/payModes/new")
    public void addPayModes(@RequestBody PayModeRequest payModeRequest){
    PaymentModes paymentModes = new PaymentModes();
    paymentModes.setName(payModeRequest.name);
    paymentModes.setType(payModeRequest.paymentMode);
         paymentModesService.addPaymentModes(paymentModes);
         System.out.println(payModeRequest);
    }
    @PutMapping("/payModes/delete/{payModesID}")
    public  void deletePayModes(@PathVariable("payModesID") Integer id){

        paymentModesService.delPaymentModes(id);
    }
    public record PayModeRequest(
            String name,
            PaymentMode paymentMode){}
}
