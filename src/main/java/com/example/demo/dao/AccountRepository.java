package com.example.demo.dao;

import com.example.demo.beans.Account;
import com.example.demo.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    //TODO FOR FOLLOWING WILL HAVE TO DO IT USING @QUERY CAUSE OF THEIR NAME (STARTING WITH 'is'😢😢😢)
//    Optional<Account> findAccountByUsernameAndAccountNonLockedIsTrue(String login); // retrieve all non-locked user accounts
    Optional<Account> findAccountByUsername(String username); // retrieve all non-locked user accounts
//    Optional<Account> findAccountByAccountNonLocked(String login);
   // List<Account> findAccountByEnabledIsTrue(); // retrieve all not-deleted users
    //Return all not locked accounts for authentication Purpose
    List<Account> findAccountByRole(RoleType roleType);
    List<Account> findAccountByEnabledIsTrue();
    List<Account> findAccountByEnabledIsTrueAndAccountNonLockedIsTrue();

}
