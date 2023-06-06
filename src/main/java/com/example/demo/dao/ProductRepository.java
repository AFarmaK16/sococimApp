package com.example.demo.dao;

import com.example.demo.beans.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findProductByValidityIsTrue();
//    List<Product> findProductByProduct_validityTrue();
}
