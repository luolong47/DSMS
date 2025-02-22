package io.github.luolong47.controller;

import io.github.luolong47.dto.ProductBatchDTO;
import io.github.luolong47.service.ProductBatchService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/product-batches")
public class ProductBatchController {
    private final ProductBatchService productBatchService;
    
    public ProductBatchController(ProductBatchService productBatchService) {
        this.productBatchService = productBatchService;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductBatchDTO>> getAllProductBatches() {
        return ResponseEntity.ok(productBatchService.getAllProductBatches());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductBatchDTO>> searchProductBatches(
            @RequestParam(required = false) String productNameEn,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
        return ResponseEntity.ok(productBatchService.searchProductBatches(productNameEn, batchNo, startDate, endDate));
    }
    
    @GetMapping("/{productBatchId}")
    public ResponseEntity<ProductBatchDTO> getProductBatch(@PathVariable String productBatchId) {
        return ResponseEntity.ok(productBatchService.getProductBatch(productBatchId));
    }
    
    @PostMapping
    public ResponseEntity<ProductBatchDTO> createProductBatch(@Valid @RequestBody ProductBatchDTO productBatchDTO) {
        return ResponseEntity.ok(productBatchService.createProductBatch(productBatchDTO));
    }
    
    @DeleteMapping("/{productBatchId}")
    public ResponseEntity<Void> deleteProductBatch(@PathVariable String productBatchId) {
        productBatchService.deleteProductBatch(productBatchId);
        return ResponseEntity.ok().build();
    }
} 