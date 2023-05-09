package com.example.demo.dao;

import com.example.demo.beans.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,Integer> {
}
