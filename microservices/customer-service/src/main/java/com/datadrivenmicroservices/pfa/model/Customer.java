package com.datadrivenmicroservices.pfa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    @CreatedDate
    private Instant customerRegistrationDate;
}
