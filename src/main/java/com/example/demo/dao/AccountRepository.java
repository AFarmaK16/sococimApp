package com.example.demo.dao;

import com.example.demo.beans.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    Optional<Account> findAccountByUsername(String login);
}
