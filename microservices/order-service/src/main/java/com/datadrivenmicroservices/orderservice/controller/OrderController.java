package com.datadrivenmicroservices.orderservice.controller;

import com.datadrivenmicroservices.orderservice.messaging.MQConfig;
import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import com.datadrivenmicroservices.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/test-rabbit")
    public ResponseEntity<?> testRabbit() throws IOException {
        File f = new File("src/main/resources/order.rdf");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line;
        String xml = "";
        while ((line = br.readLine()) != null){
            xml += line;
        }
        System.out.println(xml);

        rabbitTemplate.convertAndSend(MQConfig.RDF_EXCHANGE, MQConfig.ORDER_RDF_ROUTING_KEY, xml);
        return ResponseEntity.ok().body("RabbitMQ Test");
    }


    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order){
        OrderEntity savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody OrderEntity order, @PathVariable Long orderId){
        OrderEntity savedOrder = orderService.updateOrder(order, orderId);
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
