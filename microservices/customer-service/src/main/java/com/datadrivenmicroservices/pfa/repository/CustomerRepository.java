package com.datadrivenmicroservices.pfa.repository;

import com.datadrivenmicroservices.pfa.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
