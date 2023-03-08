package com.example.demo.services;

import com.example.demo.beans.Product;
import com.example.demo.dao.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    //READ [ALL] 😋 ❌ADMIN:CUSTOMER
    public List<Product> getProduct()
    {
        return productRepository.findAll();
    }
    //READ [ONE] 😋 ❌ADMIN/CUSTOMER
    public Product getProductByID(Integer id){
        return productRepository.findById(id).get();
    }
    //CREATE 😋 ❌ ADMIN
    public void addProduct(Product p){
        productRepository.save(p);
    }
    //DELETE 😋 ❌ ADMIN
    public void deleteProduct(Integer id){
        productRepository.deleteById(id);
    }
}
