package com.datadrivenmicroservices.pfa.controller;

import com.datadrivenmicroservices.pfa.model.Customer;
import com.datadrivenmicroservices.pfa.service.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok().body(savedCustomer);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok().body(customers);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok().body(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long customerId) {
        boolean isDeleted = customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().body(isDeleted);
    }


}
