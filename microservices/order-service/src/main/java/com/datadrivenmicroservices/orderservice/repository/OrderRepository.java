package com.datadrivenmicroservices.orderservice.repository;

import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerId(long customerId);
}
