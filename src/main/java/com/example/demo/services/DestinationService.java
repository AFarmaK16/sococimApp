package com.example.demo.services;

import com.example.demo.beans.Bank;
import com.example.demo.beans.Destination;
import com.example.demo.dao.BankRepository;
import com.example.demo.dao.DestinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;

    public List<Destination> getAllDestinations(){
        return  destinationRepository.findAll();
    }
    public void addDest(Destination destination){
        destinationRepository.save(destination);
    }
    public void delDest(Integer id){
        destinationRepository.deleteById(id);
    }

}
