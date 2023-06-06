package com.datadrivenmicroservices.orderservice.service;

import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import com.datadrivenmicroservices.orderservice.repository.OrderProductRepository;
import com.datadrivenmicroservices.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderOntologyService orderOntologyService;

    public OrderEntity saveOrder(OrderEntity order) {
        OrderEntity savedOrder = orderRepository.save(order);
        orderOntologyService.addOntologyInstance(savedOrder);
        return savedOrder;
    }

    public OrderEntity getOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        return order;
    }

    public List<OrderEntity> getOrders(){
        List<OrderEntity> orderList = orderRepository.findAll();
        return orderList;
    }

    public List<OrderEntity> getOrderByCustomerId(Long customerId){
        List<OrderEntity> orderList = orderRepository.findByForCustomer(customerId);
        return orderList;
    }

    public OrderEntity updateOrder(OrderEntity order, long orderId) {
        OrderEntity existingOrder = orderRepository.findById(orderId).orElse(null);
        if(existingOrder == null) return null;
        if(existingOrder.getHasProduct() != null) existingOrder.setHasProduct(order.getHasProduct());
        OrderEntity savedOrder = orderRepository.save(existingOrder);
        orderOntologyService.init();
        return savedOrder;
    }

    public boolean deleteOrder(Long orderId) {
        OrderEntity existingOrder = orderRepository.findById(orderId).orElse(null);
        if(existingOrder == null) return false;
        orderRepository.delete(existingOrder);
        orderOntologyService.init();
        return true;
    }
}
