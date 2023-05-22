package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.User;
import com.example.demo.dto.AccountDTO;
import com.example.demo.enums.RoleType;
import com.example.demo.services.AccountService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts/")
public class AccountController {
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final AccountService accountService ;

    @GetMapping("/account/{accountID}")
    public AccountDTO getAccountByID(@PathVariable ("accountID") Integer id){
        return  accountService.getAccount(id)
                .map(account -> new AccountDTO(
                        account.getIdAccount(),
                        account.getUsername(),
                        account.getRole(),
                        account.getUser(),
                        account.getCustomer(),
                        account.getDateOuverture())).orElseThrow(()->new UsernameNotFoundException(
                String.format("Username not found",id)
        ));

//                .orElseThrow(()->
//                System.out.println("Account with id [%s] not found".formatted(id))
//                        new ResourceNotFoundException( "Account with id %d not found".formatted(id))

    }
    @GetMapping("/account/all")
    public List<AccountDTO> getAllAccount(){
        List<AccountDTO> collect = accountService.getAllCustomers()
                .stream()
                .map(account -> new AccountDTO(
                        account.getIdAccount(),
                        account.getUsername(),
                        account.getRole(),
                        account.getUser(),
                        account.getCustomer(),
account.getDateOuverture()
//                        account.getAutorithies().stream.map(r -> r.getAutority).collect(Collectors.toList())
                )).collect(Collectors.toList());
        return collect;
    }


    @PostMapping ("/account/new")
    public void addAccount(@RequestBody AccountRequest accountRequest){

    accountService.addAcount(accountRequest);
    }
//    public void addAccount(){
//        accountService.addAcount();
//    }
    public record AccountRequest(

              String username,
              String password,
             RoleType role,
             User user,
             Customer customer

    ) {

    }
}
