package com.datadrivenmicroservices.pfa.service;

import com.datadrivenmicroservices.pfa.ProductServiceApplication;
import com.datadrivenmicroservices.pfa.model.Product;
import com.datadrivenmicroservices.pfa.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOntologyService productOntologyService;

    public Product saveProduct(Product product){
        Product savedProduct = productRepository.save(product);
        productOntologyService.addNewOntologyInstance(savedProduct); // add new ontology instance
        return savedProduct;
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product updateProduct(Product product){
        // TODO : update ontology
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        if(existingProduct == null){
            return null;
        }
        if (product.getProductName() != null) existingProduct.setProductName(product.getProductName());
        if (product.getProductDescription() != null) existingProduct.setProductDescription(product.getProductDescription());
        if (product.getProductPrice() != 0) existingProduct.setProductPrice(product.getProductPrice());
        return productRepository.save(existingProduct);
    }

    public boolean deleteProduct(Long id){
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null){
            productRepository.delete(existingProduct);
            productOntologyService.init(); // rebuild the RDF file after deleting the product
            return true;
        }
        return false;
    }
}
