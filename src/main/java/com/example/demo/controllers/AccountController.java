package com.example.demo.controllers;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.services.AccountService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts/")
public class AccountController {
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final AccountService accountService ;

    @GetMapping("/account/{accountID}")
    public Account getAccountByID(@PathVariable ("accountID") Integer id){
        return  accountService.getAccount(id);
    }

}
