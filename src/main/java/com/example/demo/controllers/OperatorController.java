package com.example.demo.controllers;

import com.example.demo.beans.Operator;
import com.example.demo.services.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class OperatorController {
    private final OperatorService operatorService;
@Autowired
    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("/operators")
    public List<Operator> getOperators(){
        return operatorService.getAllOperators();
    }

    @GetMapping("/operators/new")
    public void addOperator( OperatorRequest operatorRequest){
    Operator operator = new Operator();
    operator.setOperator_name(operatorRequest.operator_name);
         operatorService.addOperator(operator);
         System.out.println(operatorRequest);
    }
    public record OperatorRequest(
     String operator_name){}
}
