package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = this.productRepository.findAll();
        return ResponseEntity.ok(products);

    }

    @PostMapping(path = "/products")
    public ResponseEntity<String> save(@RequestBody Product product) {
        this.productRepository.save(product);
        return ResponseEntity.ok("Salvo");
    }
}
