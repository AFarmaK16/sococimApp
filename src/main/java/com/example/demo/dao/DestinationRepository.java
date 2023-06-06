package com.example.demo.dao;

import com.example.demo.beans.Destination;
import com.example.demo.beans.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Integer> {
    List<Destination> findDestinationByValidityIsTrue();

}
