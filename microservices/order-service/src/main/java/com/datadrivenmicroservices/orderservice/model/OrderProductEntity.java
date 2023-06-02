package com.datadrivenmicroservices.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long _id;
    private long productId;
    private int quantity;
}
