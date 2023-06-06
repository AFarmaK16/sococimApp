package com.example.demo.services;

import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.beans.User;
import com.example.demo.controllers.AccountController.AccountRequest;
import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.CustomerRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.enums.RoleType;
import com.example.demo.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private JwtService jwtService;

    //TODO WILL HAVE TO CHANGE IT AFTER TO VOID CAUSE NO NEED A TOKEN IF ACCOUNT IS CREATED BY ANOTHER
    //wILL HAVE TO RENAME IT REGISTER
    public AuthenticationResponse addCustomer(AccountRequest accountRequest,Customer customer){

        Account account = new Account();
        account.setUsername(accountRequest.username());
//        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode(accountRequest.password()));

        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);
//        account.setGrantedAuthorities((GrantedAuthority) customer.getGrantedAuthorities());
           System.out.println(RoleType.CUSTOMER.name());
            Customer c = new Customer();
            c.setAddress(customer.getAddress());
            c.setName(customer.getName());
            c.setSurname(customer.getSurname());
            c.setPhoneNumber(customer.getPhoneNumber());
            customerRepository.save(c);
            account.setRole(RoleType.CUSTOMER);
            account.setCustomer(c);


//        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);
        //Generating token after account creation
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
    public AuthenticationResponse addUser(AccountRequest accountRequest,User user){

        Account account = new Account();
        account.setUsername(accountRequest.username());
//        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode(accountRequest.password()));

        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);
//        account.setGrantedAuthorities((GrantedAuthority) customer.getGrantedAuthorities());
// TODO double referencement entre compte et user ou client

            account.setRole(RoleType.valueOf(accountRequest.role()));
            User u = new User();
            u.setName(user.getName());
            u.setSurname(user.getSurname());
            userRepository.save(u);
            account.setUser(u);

//        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);
        //Generating token after account creation
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
    //READ [ALL] ❌ ADMIN
    public List<Account> getAllAccounts(){
        return accountRepository.findAccountByEnabledIsTrue();// retrieve all accounts exepted deleted one(including locked one)
    }
    public List<Account> getAllLockedAccounts(){
        return accountRepository.findAccountByEnabledIsTrueAndAccountNonLockedIsTrue();//retrieve enabled but
    }

    public List<Account> getAllAccountsByRole(RoleType roleType){
        return accountRepository.findAccountByRole(roleType);
    }
    //READ [ONE]  DONE😋 ❌ADMIN/CUSTOMER
    public Optional<Account> getAccount(Integer id){
        //TODO : CHECK IF ACCOUNT EXIST BEFORE RETRIEVING IT

        return accountRepository.findById(id);
    }
    public void unLockAccount(Integer id){
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setAccountNonLocked(true);
            accountRepository.save(account);
        }
    }
    public void deleteAccount(Integer id){
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setEnabled(false);
            accountRepository.save(account);
        }
    }
    public void blockAccount(Integer id){
        Account account = null;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent())  //test si le code de hashage existe
        {
            account = optionalAccount.get();
            account.setAccountNonLocked(false);
            accountRepository.save(account);
        }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        //following will be executed only if the user is successfully authenticated
        var user = accountRepository.findAccountByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
