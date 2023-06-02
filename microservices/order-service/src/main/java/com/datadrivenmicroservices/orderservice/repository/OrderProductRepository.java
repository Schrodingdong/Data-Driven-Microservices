package com.datadrivenmicroservices.orderservice.repository;

import com.datadrivenmicroservices.orderservice.model.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
}
