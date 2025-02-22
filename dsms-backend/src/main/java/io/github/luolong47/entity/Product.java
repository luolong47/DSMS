package io.github.luolong47.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product")
public class Product {
    @Id
    private String productNameEn;
    
    @Column(nullable = false)
    private String productNameCn;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SYSTEM'")
    private String lastModifiedBy;
    
    @PrePersist
    protected void onCreate() {
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