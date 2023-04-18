package com.example.demo.services;

import com.example.demo.beans.Operator;
import com.example.demo.dao.OperatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {
    public final OperatorRepository operatorRepository;

    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }
    public List<Operator> getAllOperators(){
       return  operatorRepository.findAll();
    }
    public void addOperator(Operator op){
        operatorRepository.save(op);
    }
    public void delOperator(Integer id){
        operatorRepository.deleteById(id);
    }
}
