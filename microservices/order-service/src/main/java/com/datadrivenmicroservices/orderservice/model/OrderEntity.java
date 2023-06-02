package com.datadrivenmicroservices.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;
    @CreatedDate
    private Instant orderCreationDate;
    private Long customerId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_order", referencedColumnName = "orderId")
    private List<OrderProductEntity> orderProducts;

}
