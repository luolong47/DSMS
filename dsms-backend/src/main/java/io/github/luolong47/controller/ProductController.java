package io.github.luolong47.controller;

import io.github.luolong47.dto.ProductDTO;
import io.github.luolong47.entity.Product;
import io.github.luolong47.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    
    @GetMapping("/{productNameEn}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String productNameEn) {
        return ResponseEntity.ok(productService.getProduct(productNameEn));
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }
    
    @PutMapping("/{productNameEn}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String productNameEn,
            @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productNameEn, productDTO));
    }
    
    @DeleteMapping("/{productNameEn}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productNameEn) {
        productService.deleteProduct(productNameEn);
        return ResponseEntity.ok().build();
    }
}