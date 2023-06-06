package com.datadrivenmicroservices.pfa.controller;

import com.datadrivenmicroservices.pfa.messaging.MQConfig;
import com.datadrivenmicroservices.pfa.model.Customer;
import com.datadrivenmicroservices.pfa.service.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/test-rabbit")
    public ResponseEntity<?> testRabbit() throws IOException {
        File f = new File("src/main/resources/customer.rdf");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line;
        String xml = "";
        while ((line = br.readLine()) != null){
            xml += line;
        }
        System.out.println(xml);

        rabbitTemplate.convertAndSend(MQConfig.RDF_EXCHANGE, MQConfig.CUSTOMER_RDF_ROUTING_KEY, xml);
        return ResponseEntity.ok().body("RabbitMQ Test");
    }


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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Long customerId){
        Customer updatedCustomer = customerService.updateCustomer(customer, customerId);
        return ResponseEntity.ok().body(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long customerId) {
        boolean isDeleted = customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().body(isDeleted);
    }


}
