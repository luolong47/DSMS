package io.github.luolong47.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_batch")
public class Batch {
    @Id
    @Column(nullable = false)
    private String batchNo;
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column(nullable = false)
    private LocalDateTime endDate;
    
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