package com.example.demo.dao;

import com.example.demo.beans.Tarification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarificationRepository extends JpaRepository<Tarification,Integer> {
}
