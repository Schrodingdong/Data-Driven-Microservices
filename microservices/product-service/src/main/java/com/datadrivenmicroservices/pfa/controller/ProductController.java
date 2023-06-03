package com.datadrivenmicroservices.pfa.controller;

import com.datadrivenmicroservices.pfa.model.Product;
import com.datadrivenmicroservices.pfa.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody Product product){
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok().body(savedProduct);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getProducts(){
        List<Product> productList = productService.getProducts();
        return ResponseEntity.ok().body(productList);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product product){
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        boolean isDeleted = productService.deleteProduct(id);
        return ResponseEntity.ok().body(isDeleted);
    }
}
