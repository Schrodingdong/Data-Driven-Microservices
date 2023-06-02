package com.datadrivenmicroservices.orderservice.controller;

import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import com.datadrivenmicroservices.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order){
        OrderEntity savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody OrderEntity order){
        OrderEntity savedOrder = orderService.updateOrder(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId){
        OrderEntity order = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getOrders(){
        List<OrderEntity> orderList = orderService.getOrders();
        return ResponseEntity.ok().body(orderList);
    }

    @GetMapping("/get/customer/{customerId}")
    public ResponseEntity<?> getOrderByCustomerId(@PathVariable Long customerId){
        List<OrderEntity> orderList = orderService.getOrderByCustomerId(customerId);
        return ResponseEntity.ok().body(orderList);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId){
        boolean isDeleted = orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(isDeleted);
    }

}
