package com.example.demo.services;

import com.example.demo.beans.Product;
import com.example.demo.beans.Tarification;
import com.example.demo.dao.TarificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarificationService {
    public final TarificationRepository tarificationRepository;

    public TarificationService(TarificationRepository tarificationRepository) {
        this.tarificationRepository = tarificationRepository;
    }
    public List<Tarification> getAllTarifications(){
       return  tarificationRepository.findTarificationByValidityIsTrue();
    }
    public Tarification getTarificationByID(Integer id){
        Optional<Tarification> tarification = tarificationRepository.findById(id);
        if (tarification.isPresent()) //check if tarification exist
            return tarification.get() ;
        else
            return null;
    }
    public void addTarification(Tarification tarification){
        tarification.setValidity(true);
        tarificationRepository.save(tarification);
    }
    public void delTarification(Integer id){
        Tarification tarification = null;
        Optional<Tarification> optionalTarification = tarificationRepository.findById(id);
        if(optionalTarification.isPresent())  //test si le code de hashage existe
        {
            tarification = optionalTarification.get();
            tarification.setValidity(false);
            tarificationRepository.save(tarification);
        }
    }
//    public void delTarification(Integer id){
//        tarificationRepository.deleteById(id);
//    }
}
