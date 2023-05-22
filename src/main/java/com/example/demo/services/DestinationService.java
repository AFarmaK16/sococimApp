package com.example.demo.services;

import com.example.demo.beans.Bank;
import com.example.demo.beans.Destination;
import com.example.demo.beans.Tarification;
import com.example.demo.controllers.OrderSettingsController;
import com.example.demo.controllers.OrderSettingsController.DestinationRequest;
import com.example.demo.dao.BankRepository;
import com.example.demo.dao.DestinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final TarificationService tarificationService;;

    public List<Destination> getAllDestinations(){
        return  destinationRepository.findAll();
    }
    public void addDest(DestinationRequest destinationRequest){

        Tarification tarification = tarificationService.getTarificationByID(destinationRequest.tarification());
        Destination destination = new Destination();
        destination.setNom(destinationRequest.city());
        destination.setTarif(destinationRequest.tarif());
        destination.setTarification(tarification);
        destinationRepository.save(destination);
    }
    public void delDest(Integer id){
        destinationRepository.deleteById(id);
    }

}
