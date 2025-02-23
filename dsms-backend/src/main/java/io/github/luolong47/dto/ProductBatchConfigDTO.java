package io.github.luolong47.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductBatchConfigDTO {
    private String configId;
    
    @NotBlank(message = "产品批次ID不能为空")
    private String productBatchId;
    
    @NotBlank(message = "数据库配置名称不能为空")
    private String configName;
    
    // 关联信息
    private String productNameEn;
    private String productNameCn;
    private String batchNo;
    private String dbType;
    private String dbUrl;
    private String dbUsername;
    private String dbSchema;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private String lastModifiedBy;
} 