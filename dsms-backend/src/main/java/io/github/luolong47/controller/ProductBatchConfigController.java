package io.github.luolong47.controller;

import io.github.luolong47.dto.ProductBatchConfigDTO;
import io.github.luolong47.dto.ProductBatchConfigSearchDTO;
import io.github.luolong47.service.ProductBatchConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-batch-configs")
public class ProductBatchConfigController {
    private final ProductBatchConfigService productBatchConfigService;
    
    public ProductBatchConfigController(ProductBatchConfigService productBatchConfigService) {
        this.productBatchConfigService = productBatchConfigService;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductBatchConfigDTO>> getAllProductBatchConfigs() {
        return ResponseEntity.ok(productBatchConfigService.getAllProductBatchConfigs());
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<ProductBatchConfigDTO>> searchProductBatchConfigs(
            @RequestBody ProductBatchConfigSearchDTO searchDTO) {
        return ResponseEntity.ok(productBatchConfigService.searchProductBatchConfigs(searchDTO));
    }
    
    @GetMapping("/{configId}")
    public ResponseEntity<ProductBatchConfigDTO> getProductBatchConfig(@PathVariable String configId) {
        return ResponseEntity.ok(productBatchConfigService.getProductBatchConfig(configId));
    }
    
    @PostMapping
    public ResponseEntity<ProductBatchConfigDTO> createProductBatchConfig(
            @Valid @RequestBody ProductBatchConfigDTO configDTO) {
        return ResponseEntity.ok(productBatchConfigService.createProductBatchConfig(configDTO));
    }
    
    @PutMapping("/{configId}")
    public ResponseEntity<ProductBatchConfigDTO> updateProductBatchConfig(
            @PathVariable String configId,
            @Valid @RequestBody ProductBatchConfigDTO configDTO) {
        return ResponseEntity.ok(productBatchConfigService.updateProductBatchConfig(configId, configDTO));
    }
    
    @DeleteMapping("/{configId}")
    public ResponseEntity<Void> deleteProductBatchConfig(@PathVariable String configId) {
        productBatchConfigService.deleteProductBatchConfig(configId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{configId}/test-connection")
    public ResponseEntity<Boolean> testConnection(@PathVariable String configId) {
        return ResponseEntity.ok(productBatchConfigService.testConnection(configId));
    }
} 