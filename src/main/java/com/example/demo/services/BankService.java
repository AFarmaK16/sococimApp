package com.example.demo.services;

import com.example.demo.beans.Bank;
import com.example.demo.dao.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BankService {
    private final BankRepository bankRepository;

    public List<Bank> getAllBanks(){
        return  bankRepository.findAll();
    }
    public void addBank(Bank bank){
        bankRepository.save(bank);
    }
    public void delBank(Integer id){
        bankRepository.deleteById(id);
    }


}
