package io.github.luolong47.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product_batch")
public class ProductBatch {
    @Id
    private String productBatchId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_name_en", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_no", nullable = false)
    private Batch batch;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SYSTEM'")
    private String lastModifiedBy;
    
    @PrePersist
    protected void onCreate() {
        if (productBatchId == null && product != null && batch != null) {
            productBatchId = product.getProductNameEn() + "_" + batch.getBatchNo();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastModifiedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        lastModifiedBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }
} 