package io.github.luolong47.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DbConfigDTO {
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    @NotBlank(message = "数据库类型不能为空")
    private String type;

    @NotBlank(message = "数据库URL不能为空")
    private String url;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "驱动类名不能为空")
    private String driverClassName;

    private String schema;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String lastModifiedBy;
}