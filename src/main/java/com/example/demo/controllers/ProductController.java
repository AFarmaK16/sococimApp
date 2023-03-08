package com.example.demo.controllers;

import com.example.demo.beans.Customer;
import com.example.demo.beans.Product;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Product p = new Product();
        p.setProduct_label(productRequest.product_label);
        p.setProduct_price(productRequest.product_price);
        p.setProduct_type(productRequest.product_type);
        p.setProduct_description(productRequest.product_description);
        productService.addProduct(p);
    }

    @GetMapping("/lists")
    public List<Product> getCustomers(){

       /* product.add(new CustomerRequest(1,"Awa","Kane","Dubai","772097634"));
        product.add(new CustomerRequest(2,"Zaid","Kane","Guediawaye","77207634"));
        */

        return productService.getProduct();

    }
    @GetMapping("{productId}")
    public Product getProduct(@PathVariable("productId") Integer id){
        return productService.getProductByID(id);
    }
    @DeleteMapping("/delete/{productId}")
    public  void deleteProduct(@PathVariable("productId") Integer id){

        productService.deleteProduct(id);
    }
   /* @PutMapping("/update/{productId}")
    public void updateProduct(@PathVariable("productId") Integer id,@RequestBody ProductRequest productRequest){


    }*/
    record ProductRequest(
                Integer product_id,
    Double product_price,
        String product_label,
        String product_type,
        String product_description
    )
    {

    }
}
