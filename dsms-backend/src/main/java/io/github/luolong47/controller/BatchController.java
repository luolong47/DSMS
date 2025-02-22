package io.github.luolong47.controller;

import io.github.luolong47.dto.BatchDTO;
import io.github.luolong47.entity.Batch;
import io.github.luolong47.service.BatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {
    private final BatchService batchService;
    
    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }
    
    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }
    
    @GetMapping("/{batchNo}")
    public ResponseEntity<Batch> getBatch(@PathVariable String batchNo) {
        return ResponseEntity.ok(batchService.getBatch(batchNo));
    }
    
    @PostMapping
    public ResponseEntity<Batch> createBatch(@Valid @RequestBody BatchDTO batchDTO) {
        return ResponseEntity.ok(batchService.createBatch(batchDTO));
    }
    
    @PutMapping("/{batchNo}")
    public ResponseEntity<Batch> updateBatch(
            @PathVariable String batchNo,
            @Valid @RequestBody BatchDTO batchDTO) {
        return ResponseEntity.ok(batchService.updateBatch(batchNo, batchDTO));
    }
    
    @DeleteMapping("/{batchNo}")
    public ResponseEntity<Void> deleteBatch(@PathVariable String batchNo) {
        batchService.deleteBatch(batchNo);
        return ResponseEntity.ok().build();
    }
} 