package com.example.demo.services;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.dao.AccountRepository;
import com.example.demo.enums.RoleType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import static com.example.demo.enums.ApplicationUserRole.*;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    public void addAcount(){
        Account account = new Account();
        account.setUsername("lala");
        account.setAccount_validity(true);
        account.setPassword(passwordEncoder.encode("passer"));
        account.setAccountNonExpired(true);
        account.setAccountNonLocked(true);
        account.setDateOuverture(new Date());
        account.setCredentialsNonExpired(true);
        account.setEnabled(true);
//        account.setGrantedAuthorities((GrantedAuthority) customer.getGrantedAuthorities());
        account.setRole(RoleType.CUSTOMER);
        accountRepository.save(account);

    }

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
