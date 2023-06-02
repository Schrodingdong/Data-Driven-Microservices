package com.datadrivenmicroservices.pfa.service;

import com.datadrivenmicroservices.pfa.model.Product;
import com.datadrivenmicroservices.pfa.ontology.ProductOntology;
import com.datadrivenmicroservices.pfa.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductOntologyService {
    private final ProductRepository productRepository;
    private final ProductOntology productOntology;

    @PostConstruct
    public void init(){
        productOntology.initialiseModel();
        addOntologyInstances();
    }

    private void addOntologyInstances(){
        List<Product> productList = productRepository.findAll();
        if (productList.size() == 0) return;
        productOntology.addProductsFromList(productList);
    }

    public void addNewOntologyInstance(Product product){
        productOntology.addProduct(product);
    }
}
