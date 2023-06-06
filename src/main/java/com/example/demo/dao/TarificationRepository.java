package com.example.demo.dao;

import com.example.demo.beans.Tarification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarificationRepository extends JpaRepository<Tarification,Integer> {
List<Tarification> findTarificationByValidityIsTrue();
}
