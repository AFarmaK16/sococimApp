package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Product;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin // ⚡⚡⚡⚡⚡⚡ TODO : enlever ou restreindre le CORS
@RestController
@RequestMapping("/api/v1/products/")
public class ProductController {
    private final ProductService productService;

@Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/add")
    public void addProduct(@RequestBody ProductRequest productRequest){
    System.out.println(productRequest);

        productService.addProduct(productRequest);
    }

    @GetMapping("/lists")
    public List<Product> getProducts(){

       /* product.add(new CustomerRequest(1,"Awa","Kane","Dubai","772097634"));
        product.add(new CustomerRequest(2,"Zaid","Kane","Guediawaye","77207634"));
        */

        return productService.getProduct();

    }
    @GetMapping("/lists/{productId}")
    public Product getProduct(@PathVariable("productId") Integer id){
        return productService.getProductByID(id);
    }
    @PutMapping("/delete/{productId}")
    public  void deleteProduct(@PathVariable("productId") Integer id){

        productService.deleteProduct(id);
    }
    @PutMapping("/update/{productId}")
    public void updateProduct(@PathVariable("productId") Integer id,@RequestBody ProductRequest productRequest){
    System.out.println(productRequest);
    productService.updateProduct(id,productRequest);

    }
   /* @PutMapping("/update/{productId}")
    public void updateProduct(@PathVariable("productId") Integer id,@RequestBody ProductRequest productRequest){


    }*/
   public record ProductRequest(
//                Integer product_id,
        String product_label,
        String product_type,
        String product_description,
    String product_imageURI,
    Integer tarification
    )
    {

    }
}
