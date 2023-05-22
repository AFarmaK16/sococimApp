package com.example.demo.dao;

import com.example.demo.beans.Account;
import com.example.demo.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    Optional<Account> findAccountByUsername(String login);
    List<Account> findAccountByRole(RoleType roleType);
}
