package com.datadrivenmicroservices.pfa.service;

import com.datadrivenmicroservices.pfa.model.Customer;
import com.datadrivenmicroservices.pfa.ontology.CustomerOntology;
import com.datadrivenmicroservices.pfa.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerOntologyService {
    private final CustomerOntology customerOntology;
    private final CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        customerOntology.initialiseModel();
        addOntologyInstances();
    }

    private void addOntologyInstances(){
        List<Customer> customerList = customerRepository.findAll();
        if(customerList.size() == 0) return;
        customerOntology.addCustomersFromList(customerList);
    }

    public void addOntologyInstance(Customer customer){
        customerOntology.addCustomer(customer);
    }
}
