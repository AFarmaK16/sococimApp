package com.example.demo.services;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.dao.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    //READ [ALL] ❌ ADMIN
    public List<Account> getAllCustomers(){
        return accountRepository.findAll();
    }
    //READ [ONE]  DONE😋 ❌ADMIN/CUSTOMER
    public Account getAccount(Integer id){
        //TODO : CHECK IF ACCOUNT EXIST BEFORE RETRIEVING IT
        return accountRepository.findById(id).get();
    }
}
