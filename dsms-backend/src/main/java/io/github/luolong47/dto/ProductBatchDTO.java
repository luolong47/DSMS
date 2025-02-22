package io.github.luolong47.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductBatchDTO {
    private String productBatchId;
    
    @NotBlank(message = "产品英文名称不能为空")
    private String productNameEn;
    
    @NotBlank(message = "批次号不能为空")
    private String batchNo;
    
    private String productNameCn;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime batchStartDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime batchEndDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private String lastModifiedBy;
} 