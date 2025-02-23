package io.github.luolong47.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product_batch_config")
public class ProductBatchConfig {
    @Id
    private String configId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_batch_id", nullable = false)
    private ProductBatch productBatch;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_name", nullable = false)
    private DbConfig dbConfig;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SYSTEM'")
    private String lastModifiedBy;
    
    @PrePersist
    protected void onCreate() {
        if (configId == null && productBatch != null && dbConfig != null) {
            configId = productBatch.getProductBatchId() + "_" + dbConfig.getConfigName();
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