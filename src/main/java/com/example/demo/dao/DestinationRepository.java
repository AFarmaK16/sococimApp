package com.example.demo.dao;

import com.example.demo.beans.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination,Integer> {
}
