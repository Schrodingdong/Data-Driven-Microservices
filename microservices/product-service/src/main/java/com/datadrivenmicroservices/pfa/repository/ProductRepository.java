package com.datadrivenmicroservices.pfa.repository;

import com.datadrivenmicroservices.pfa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
