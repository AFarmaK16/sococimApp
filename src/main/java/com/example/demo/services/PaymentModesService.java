package com.example.demo.services;

import com.example.demo.beans.PaymentModes;
import com.example.demo.dao.PaymentModesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentModesService {
    public final PaymentModesRepository paymentModesRepository;

    public PaymentModesService(PaymentModesRepository paymentModesRepository) {
        this.paymentModesRepository = paymentModesRepository;
    }
    public List<com.example.demo.beans.PaymentModes> getAllPaymentModess(){
       return  paymentModesRepository.findPaymentModesByValidityTrue();
    }
    public com.example.demo.beans.PaymentModes getPaymentModesByID(Integer id){
        Optional<com.example.demo.beans.PaymentModes> operator = paymentModesRepository.findById(id);
        if (operator.isPresent()) //check if operator exist
            return operator.get() ;
        else
            return null;
    }
    public void addPaymentModes(com.example.demo.beans.PaymentModes op){
        op.setValidity(true);
        paymentModesRepository.save(op);
    }
    public void delPaymentModes(Integer id){
        paymentModesRepository.deleteById(id);
        PaymentModes paymentModes = null;
        Optional<PaymentModes> optionalPaymentModes = paymentModesRepository.findById(id);
        if(optionalPaymentModes.isPresent())  //test si le code de hashage existe
        {
            paymentModes = optionalPaymentModes.get();
            paymentModes.setValidity(false);
            paymentModesRepository.save(paymentModes);
        }
    }

}
