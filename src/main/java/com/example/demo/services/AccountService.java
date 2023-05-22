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
    public AuthenticationResponse addAcount(AccountRequest accountRequest){

        Account account = new Account();
        account.setUsername(accountRequest.username());
        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode(accountRequest.password()));
        account.setRole(accountRequest.role());
        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);
//        account.setGrantedAuthorities((GrantedAuthority) customer.getGrantedAuthorities());
        if (accountRequest.role().equals(RoleType.CUSTOMER)){
            Customer customer = new Customer();
            customer.setCustomerAddress(accountRequest.customer().getCustomerAddress());
            customer.setCustomerFirstName(accountRequest.customer().getCustomerFirstName());
            customer.setCustomerLastName(accountRequest.customer().getCustomerLastName());
            customer.setCustomerPhoneNumber(accountRequest.customer().getCustomerPhoneNumber());
            customerRepository.save(customer);
            account.setCustomer(customer);
        }
        else {
            User user = new User();
            user.setName(accountRequest.user().getName());
            user.setSurname(accountRequest.user().getSurname());
            userRepository.save(user);
            account.setUser(user);
        }

//        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);
        //Generating token after account creation
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    //READ [ALL] ❌ ADMIN
    public List<Account> getAllCustomers(){
        return accountRepository.findAll();
    }
    public List<Account> getAllAccountsByRole(RoleType roleType){
        return accountRepository.findAccountByRole(roleType);
    }
    //READ [ONE]  DONE😋 ❌ADMIN/CUSTOMER
    public Optional<Account> getAccount(Integer id){
        //TODO : CHECK IF ACCOUNT EXIST BEFORE RETRIEVING IT

        return accountRepository.findById(id);
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
