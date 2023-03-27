package com.example.demo.dao;

import com.example.demo.beans.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Integer> {
}
