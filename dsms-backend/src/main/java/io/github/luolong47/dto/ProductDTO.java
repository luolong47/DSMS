package io.github.luolong47.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank(message = "英文名称不能为空")
    @Size(max = 255, message = "英文名称长度不能超过255个字符")
    private String productNameEn;
    
    @NotBlank(message = "中文名称不能为空")
    @Size(max = 255, message = "中文名称长度不能超过255个字符")
    private String productNameCn;
    
    @Size(max = 1000, message = "描述长度不能超过1000个字符")
    private String description;
}