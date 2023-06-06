package com.example.demo.services;

import com.example.demo.beans.PaymenType;
import com.example.demo.beans.Product;
import com.example.demo.beans.Tarification;
import com.example.demo.controllers.ProductController;
import com.example.demo.dao.PaymentTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@AllArgsConstructor
public class PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;
    public List<PaymenType> getAllPMode(){
        return  paymentTypeRepository.findPaymenTypeByValidityIsTrue();
    }
    public void addPayType(PaymenType paymenType){
        paymenType.setValidity(true);

        paymentTypeRepository.save(paymenType);
    }


    public void delPayType(Integer id){
        PaymenType paymenType = null;
        Optional<PaymenType> optionalPaymenType = paymentTypeRepository.findById(id);
        if(optionalPaymenType.isPresent())  //test si le code de hashage existe
        {
            paymenType = optionalPaymenType.get();
            paymenType.setValidity(false);
            paymentTypeRepository.save(paymenType);
        }
    }
    //UPDATE 😋 ❌ ADMIN
//ON NE MODIFIE PAS UN PAYMENT-TYPE JUST INVALIDATE(SUPP)
}
