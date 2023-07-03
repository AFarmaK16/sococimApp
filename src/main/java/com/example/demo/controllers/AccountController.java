package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.User;
import com.example.demo.dto.AccountDTO;
import com.example.demo.services.AccountService;
import com.example.demo.services.EmailService;
import com.example.demo.services.PasswordGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.constant.EmailConstant.DELIVERY_ORDER_EMAIL_SUBJECT;
import static com.example.demo.constant.EmailConstant.NEW_ACCOUNT_EMAIL_SUBJECT;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts/")
public class AccountController {
    public AccountController(AccountService accountService, EmailService emailService) {
        this.accountService = accountService;
        this.emailService = emailService;
    }

    private final AccountService accountService ;
    private final EmailService emailService;

    @GetMapping("/account/{accountID}")
    public AccountDTO getAccountByID(@PathVariable ("accountID") Integer id){
        return  accountService.getAccount(id)
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getUsername(),
                        account.getRole(),
                        account.getUser(),
                        account.getCustomer(),
                        account.getDateOuverture(),
                        account.isEnabled(),
                        account.isAccountNonLocked()
                )).orElseThrow(()->new UsernameNotFoundException(
                String.format("Username not found",id)
        ));

//                .orElseThrow(()->
//                System.out.println("Account with id [%s] not found".formatted(id))
//                        new ResourceNotFoundException( "Account with id %d not found".formatted(id))

    }
    @GetMapping("/account/all")
    public List<AccountDTO> getAllAccounts(){
        List<AccountDTO> collect = accountService.getAllAccounts()
                .stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getUsername(),
                        account.getRole(),
                        account.getUser(),
                        account.getCustomer(),
account.getDateOuverture(),
                        account.isEnabled(),
                        account.isAccountNonLocked()
//                        account.getAutorithies().stream.map(r -> r.getAutority).collect(Collectors.toList())
                )).collect(Collectors.toList());
        return collect;
    }


    @PostMapping ("/account/new")
    private ResponseEntity<?> addAccount(@ModelAttribute("accountRequest")  AccountRequest accountRequest, @RequestParam("user") String user) throws JsonProcessingException, MessagingException {
        System.out.println(accountRequest);
        System.out.println(user);
        String generatedPassword = PasswordGenerator.generateStrongPassword(12); // Change the length as needed

        //Mapping Facture String to FactureRequest
        ObjectMapper mapper1 = new ObjectMapper();
        User user1 = mapper1.readValue(user, new TypeReference<>() {});
        //END MAPPING


        accountService.addUser(accountRequest,user1,generatedPassword);
        String email_text = "Votre compte   "+ accountRequest.role()+" a été crée avec succés "+
                "\n Mot de passe par défaut : \n "+ generatedPassword+
                "\n Veuillez le changer a la premiere authentification.";

        emailService.sendEmail(user1.getName(),accountRequest.username,email_text,NEW_ACCOUNT_EMAIL_SUBJECT);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping ("/account/customer/new")
    private ResponseEntity<?> addCustomerAccount(@ModelAttribute("accountRequest")  AccountRequest accountRequest,@RequestParam("customer") String customer) throws JsonProcessingException, MessagingException {
        //Mapping Facture String to FactureRequest
     System.out.println(accountRequest);
     System.out.println(customer);
        ObjectMapper mapper1 = new ObjectMapper();
        Customer customer1 = mapper1.readValue(customer, new TypeReference<>() {
        });
        //END MAPPING
        String generatedPassword = PasswordGenerator.generateStrongPassword(12); // Change the length as needed


        accountService.addCustomer(accountRequest,customer1,generatedPassword);
        String email_text = "Votre compte   "+ accountRequest.role()+" a été crée avec succés "+
                "\n Mot de passe par défaut : \n"+ generatedPassword+
                "\n Veuillez le changer a la premiere authentification.";

        emailService.sendEmail(customer1.getName(),accountRequest.username,email_text,NEW_ACCOUNT_EMAIL_SUBJECT);

//        accountService.addAcount(accountRequest,"CUSTOMER");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/delete/{accountID}")
    private ResponseEntity<?> deleteAccount(@PathVariable("accountID") Integer id){
        System.out.println(id +"ba gui nii dh for deleting purpose 😍😍😍✔");
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/block/{accountID}")
    private ResponseEntity<?> blockAccount(@PathVariable("accountID") Integer id){
        System.out.println(id +"ba gui nii dh 😋😋😋 bloquel");

        accountService.blockAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/unlock/{accountID}")
    private ResponseEntity<?> unLockAccount(@PathVariable("accountID") Integer id){
        System.out.println(id +"ba gui nii dh 😋😋😋 bloquel");

        accountService.unLockAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/account/customer/reset-password/{accountID}")
    public ResponseEntity<?> resetPassword(@PathVariable("accountID") Integer id, @RequestBody PasswordResetRequest resetRequest) {

        accountService.resetPassword(id,resetRequest.newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/account/update/{accountID}")
    public ResponseEntity<?> updateAccount(@PathVariable("accountID") Integer id, @RequestBody UpdateAccountRequest updateAccountRequest) {
        System.out.println(updateAccountRequest);
        accountService.updateAccount(id,updateAccountRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public record PasswordResetRequest(

            String newPassword
    ) {
        // Getters and setters if needed
    }


    public record AccountRequest(

              String username,
              String role

//             User user,
//             Customer customer

    ) {

    }
    public record UpdateAccountRequest(

            String address,
            String newPassword,
            String role

//             User user,
//             Customer customer

    ) {

    }

}
