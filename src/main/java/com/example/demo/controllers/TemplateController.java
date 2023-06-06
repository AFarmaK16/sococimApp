package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@CrossOrigin
@Controller
public class TemplateController {
    @GetMapping("login")
    public  String getLoginView(){
        return "login";
    }
    @GetMapping("products")
    public  String getProducts(){
        return "products";
    }
}
