package io.github.luolong47.service;

import io.github.luolong47.dto.DbConfigDTO;
import io.github.luolong47.dto.DbConfigSearchDTO;
import io.github.luolong47.entity.DbConfig;
import io.github.luolong47.repository.DbConfigRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbConfigService {
    private final DbConfigRepository dbConfigRepository;

    public List<DbConfig> search(DbConfigSearchDTO searchDTO) {
        Specification<DbConfig> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            
            if (searchDTO.getConfigName() != null && !searchDTO.getConfigName().isEmpty()) {
                predicates.add(cb.like(root.get("configName"), "%" + searchDTO.getConfigName() + "%"));
            }
            if (searchDTO.getUrl() != null && !searchDTO.getUrl().isEmpty()) {
                predicates.add(cb.like(root.get("url"), "%" + searchDTO.getUrl() + "%"));
            }
            if (searchDTO.getUsername() != null && !searchDTO.getUsername().isEmpty()) {
                predicates.add(cb.like(root.get("username"), "%" + searchDTO.getUsername() + "%"));
            }
            
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        
        return dbConfigRepository.findAll(spec);
    }

    @Transactional
    public DbConfig save(DbConfigDTO dto) {
        DbConfig config = new DbConfig();
        config.setConfigName(dto.getConfigName());
        config.setType(dto.getType());
        config.setUrl(dto.getUrl());
        config.setUsername(dto.getUsername());
        config.setPassword(dto.getPassword());
        config.setDriverClassName(dto.getDriverClassName());
        config.setSchema(dto.getSchema());
        config.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        
        return dbConfigRepository.save(config);
    }

    @Transactional
    public void delete(String configName) {
        if (!dbConfigRepository.existsById(configName)) {
            throw new EntityNotFoundException("数据库配置不存在：" + configName);
        }
        dbConfigRepository.deleteById(configName);
    }

    public boolean testConnection(DbConfigDTO dto) {
        try {
            Class.forName(dto.getDriverClassName());
            try (Connection conn = DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPassword())) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库连接测试失败：" + e.getMessage());
        }
    }
} 