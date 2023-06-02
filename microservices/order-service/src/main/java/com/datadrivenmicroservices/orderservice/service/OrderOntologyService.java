package com.datadrivenmicroservices.orderservice.service;

import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import com.datadrivenmicroservices.orderservice.ontology.OrderOntology;
import com.datadrivenmicroservices.orderservice.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderOntologyService {
    private final OrderOntology orderOntology;
    private final OrderRepository orderRepository;

    @PostConstruct
    public void init() {
        orderOntology.initialiseModel();
        addOntologyInstances();
    }

    private void addOntologyInstances(){
        List<OrderEntity> customerList = orderRepository.findAll();
        if(customerList.size() == 0) return;
        orderOntology.addOrdersFromList(customerList);
    }

    public void addOntologyInstance(OrderEntity order){
        orderOntology.addOrder(order);
    }
}
