package com.example.demo.services;

import com.example.demo.beans.Destination;
import com.example.demo.beans.Product;
import com.example.demo.beans.Tarification;
import com.example.demo.controllers.OrderSettingsController.DestinationRequest;
import com.example.demo.controllers.ProductController;
import com.example.demo.dao.DestinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final TarificationService tarificationService;;

    public List<Destination> getAllDestinations(){
        return  destinationRepository.findDestinationByValidityIsTrue();
    }
    public void addDest(DestinationRequest destinationRequest){

        Tarification tarification = tarificationService.getTarificationByID(destinationRequest.tarification());
        Destination destination = new Destination();
        destination.setNom(destinationRequest.city());
        destination.setValidity(true);
//        destination.setTarif(destinationRequest.tarif());
        destination.setTarification(tarification);
        destinationRepository.save(destination);
    }
    public void delDest(Integer id){
        Destination destination = null;
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if(optionalDestination.isPresent())  //test si le code de hashage existe
        {
            destination = optionalDestination.get();
            destination.setValidity(false);
            destinationRepository.save(destination);
        }
//        destinationRepository.deleteById(id);
    }
    public void updateDest(Integer id, DestinationRequest destinationRequest){

        Tarification tarification = tarificationService.getTarificationByID(destinationRequest.tarification());
        Destination d = null;
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if(optionalDestination.isPresent())  //test si le code de hashage existe
        {
            d = optionalDestination.get();
            d.setTarification(tarification);
            destinationRepository.save(d);
        }

    }

}
