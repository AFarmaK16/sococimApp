package com.example.demo.services;

import com.example.demo.beans.Orders;
import com.example.demo.beans.Product;
import com.example.demo.beans.Tarification;
import com.example.demo.controllers.ProductController.ProductRequest;
import com.example.demo.dao.ProductRepository;
import com.example.demo.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final TarificationService tarificationService;

    public ProductService(ProductRepository productRepository, TarificationService tarificationService) {
        this.productRepository = productRepository;
        this.tarificationService = tarificationService;
    }
    //READ [ALL VALID] 😋 ❌ADMIN:CUSTOMER
    public List<Product> getProduct()
    {
        return productRepository.findProductByValidityIsTrue();
    }
    //READ [ONE] 😋 ❌ADMIN/CUSTOMER
    public Product getProductByID(Integer id){
        return productRepository.findById(id).get();
    }
    //CREATE 😋 ❌ ADMIN
    public void addProduct(ProductRequest productRequest){
        Tarification tarification = tarificationService.getTarificationByID(productRequest.tarification());
        Product p = new Product();
        p.setValidity(true);
        p.setProduct_label(productRequest.product_label());
//        p.setProduct_price(productRequest.product_price());
        p.setProduct_type(productRequest.product_type());
        p.setProduct_description(productRequest.product_description());
        p.setTarification(tarification);
        p.setProduct_imageURI(null);
        productRepository.save(p);
    }
    //DELETE 😋 ❌ ADMIN
    public void deleteProduct(Integer id){
        Product product = null;
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent())  //test si le code de hashage existe
        {
            product = optionalProduct.get();
            product.setValidity(false);
            productRepository.save(product);
        }
    }
    //UPDATE 😋 ❌ ADMIN
       public void updateProduct(Integer id,ProductRequest request){

           Tarification tarification = tarificationService.getTarificationByID(request.tarification());
        Product p = null;
           Optional<Product> optionalProduct = productRepository.findById(id);
           if(optionalProduct.isPresent())  //test si le code de hashage existe
           {
               p = optionalProduct.get();
               p.setProduct_type(request.product_type());
               p.setProduct_description(request.product_description());
               p.setTarification(tarification);
               p.setProduct_imageURI(null);
               productRepository.save(p);
           }

    }

}
