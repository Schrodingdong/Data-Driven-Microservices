package com.datadrivenmicroservices.pfa.service;

import com.datadrivenmicroservices.pfa.model.Customer;
import com.datadrivenmicroservices.pfa.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerOntologyService customerOntologyService;

    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        customerOntologyService.addOntologyInstance(savedCustomer);
        return savedCustomer;
    }

    public Customer getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        return customer;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Customer updateCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId()).orElse(null);
        if (existingCustomer == null) {
            return null;
        }
        if(customer.getCustomerFirstName() != null) existingCustomer.setCustomerFirstName(customer.getCustomerFirstName());
        if(customer.getCustomerLastName() != null) existingCustomer.setCustomerLastName(customer.getCustomerLastName());
        if(customer.getCustomerEmail() != null) existingCustomer.setCustomerEmail(customer.getCustomerEmail());
        if(customer.getCustomerRegistrationDate() != null) existingCustomer.setCustomerRegistrationDate(customer.getCustomerRegistrationDate());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        customerOntologyService.init(); // rebuild the RDF file after updating the product
        return updatedCustomer;
    }

    public boolean deleteCustomer(Long customerId) {
        Customer existingCustomer = customerRepository.findById(customerId).orElse(null);
        if (existingCustomer == null) return false;
        customerRepository.deleteById(customerId);
        customerOntologyService.init(); // rebuild the RDF file after deleting the product
        return true;
    }
}
