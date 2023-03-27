package com.example.demo.services;

import com.example.demo.beans.Account;
import com.example.demo.beans.Customer;
import com.example.demo.dao.AccountRepository;
import com.example.demo.dao.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
//TODO Veillez a ne pas afficher le password d'un customer ou de n'importe quel utilisateur
    //READ [ALL]-😋 ❌ ADMIN
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    //READ [ONE]-😋 ❌ADMIN:CUSTOMER
    public Customer getCustomer(Integer id){
        return customerRepository.findById(id).get();
    }
    //CREATE
    //TODO avoid creating a user with same information and more than one account and RETURN BOOLEAN AFTER CREATING
    //😋
    public void createCustomer(Customer customer){
       /* Account account = new Account();
        //account.setIdAccount(customer.getAccount().getIdAccount());
        account.setRole(customer.getAccount().getRole());
        account.setLogin(customer.getAccount().getLogin());
        account.setPassword(customer.getAccount().getPassword());
        account.setDateOuverture(customer.getAccount().getDateOuverture());
        account.setUserRefID(customer.getAccount().getUserRefID());*/
        //accountRepository.save(account);

        customerRepository.save(customer);
    }
    //DELETE TODO REPLACE DELETE CUSTOMER BY SETTING IT'S VALIDITY ATTRIBUTE TO 0
    public void deleteCustomer(Integer id){
        if (!customerRepository.existsById(id)){
            throw new IllegalStateException("Customer with Id: "+id+" doesn't exists");
        }
        customerRepository.deleteById(id);
        accountRepository.deleteById(customerRepository.findById(id).get().getAccount().getIdAccount());
    }
    //UPDATE
    /*public void updateCustomer(Customer request,Integer id){
        Customer c = customerRepository.findById(id).get();
        c.setCustomerFirstName(request.getCustomerFirstName());
        c.setCustomerLastName(request.getCustomerLastName());
        c.setCustomerAddress(request.getCustomerAddress());
        c.setCustomerPhoneNumber(request.getCustomerPhoneNumber());
        customerRepository.save(c);

    }*/


}
