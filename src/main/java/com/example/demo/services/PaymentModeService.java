package com.example.demo.services;

import com.example.demo.beans.Destination;
import com.example.demo.beans.PaymentMode;
import com.example.demo.dao.DestinationRepository;
import com.example.demo.dao.PaymentModeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@AllArgsConstructor
public class PaymentModeService {
    private final PaymentModeRepository paymentModeRepository;
    public List<PaymentMode> getAllPMode(){
        return  paymentModeRepository.findAll();
    }
    public void addPMode(PaymentMode paymentMode){
        paymentMode.setValidity(true);

        paymentModeRepository.save(paymentMode);
    }
    public void delPMode(Integer id){
        paymentModeRepository.deleteById(id);
    }

}
