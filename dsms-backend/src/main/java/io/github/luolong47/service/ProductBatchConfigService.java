package io.github.luolong47.service;

import io.github.luolong47.dto.ProductBatchConfigDTO;
import io.github.luolong47.dto.ProductBatchConfigSearchDTO;
import io.github.luolong47.entity.*;
import io.github.luolong47.repository.DbConfigRepository;
import io.github.luolong47.repository.ProductBatchConfigRepository;
import io.github.luolong47.repository.ProductBatchRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBatchConfigService {
    private final ProductBatchConfigRepository productBatchConfigRepository;
    private final ProductBatchRepository productBatchRepository;
    private final DbConfigRepository dbConfigRepository;
    
    public ProductBatchConfigService(
            ProductBatchConfigRepository productBatchConfigRepository,
            ProductBatchRepository productBatchRepository,
            DbConfigRepository dbConfigRepository) {
        this.productBatchConfigRepository = productBatchConfigRepository;
        this.productBatchRepository = productBatchRepository;
        this.dbConfigRepository = dbConfigRepository;
    }
    
    public List<ProductBatchConfigDTO> getAllProductBatchConfigs() {
        return productBatchConfigRepository.findAllWithDetails()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductBatchConfigDTO> searchProductBatchConfigs(ProductBatchConfigSearchDTO searchDTO) {
        Specification<ProductBatchConfig> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Join<ProductBatchConfig, ProductBatch> productBatchJoin = root.join("productBatch", JoinType.INNER);
            Join<ProductBatch, Product> productJoin = productBatchJoin.join("product", JoinType.INNER);
            Join<ProductBatch, Batch> batchJoin = productBatchJoin.join("batch", JoinType.INNER);
            Join<ProductBatchConfig, DbConfig> dbConfigJoin = root.join("dbConfig", JoinType.INNER);
            
            if (searchDTO.getProductNameEn() != null && !searchDTO.getProductNameEn().isEmpty()) {
                predicates.add(cb.like(cb.lower(productJoin.get("productNameEn")), 
                    "%" + searchDTO.getProductNameEn().toLowerCase() + "%"));
            }
            
            if (searchDTO.getBatchNo() != null && !searchDTO.getBatchNo().isEmpty()) {
                predicates.add(cb.like(cb.lower(batchJoin.get("batchNo")), 
                    "%" + searchDTO.getBatchNo().toLowerCase() + "%"));
            }
            
            if (searchDTO.getConfigName() != null && !searchDTO.getConfigName().isEmpty()) {
                predicates.add(cb.like(cb.lower(dbConfigJoin.get("configName")), 
                    "%" + searchDTO.getConfigName().toLowerCase() + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return productBatchConfigRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ProductBatchConfigDTO getProductBatchConfig(String configId) {
        ProductBatchConfig config = getProductBatchConfigEntity(configId);
        return convertToDTO(config);
    }
    
    @Transactional
    public ProductBatchConfigDTO createProductBatchConfig(ProductBatchConfigDTO configDTO) {
        ProductBatch productBatch = productBatchRepository.findById(configDTO.getProductBatchId())
            .orElseThrow(() -> new RuntimeException("产品批次不存在"));
            
        DbConfig dbConfig = dbConfigRepository.findById(configDTO.getConfigName())
            .orElseThrow(() -> new RuntimeException("数据库配置不存在"));
            
        // 检查是否已存在相同的配置
        String configId = configDTO.getProductBatchId() + "_" + configDTO.getConfigName();
        if (productBatchConfigRepository.existsById(configId)) {
            throw new RuntimeException("该产品批次配置已存在");
        }
            
        ProductBatchConfig config = new ProductBatchConfig();
        config.setConfigId(configId);
        config.setProductBatch(productBatch);
        config.setDbConfig(dbConfig);

        
        return convertToDTO(productBatchConfigRepository.save(config));
    }
    
    @Transactional
    public ProductBatchConfigDTO updateProductBatchConfig(String configId, ProductBatchConfigDTO configDTO) {
        ProductBatchConfig config = getProductBatchConfigEntity(configId);
        return convertToDTO(productBatchConfigRepository.save(config));
    }
    
    @Transactional
    public void deleteProductBatchConfig(String configId) {
        ProductBatchConfig config = getProductBatchConfigEntity(configId);
        productBatchConfigRepository.delete(config);
    }
    
    public boolean testConnection(String configId) {
        ProductBatchConfig config = getProductBatchConfigEntity(configId);
        DbConfig dbConfig = config.getDbConfig();
        
        try {
            Class.forName(dbConfig.getDriverClassName());
            try (Connection conn = DriverManager.getConnection(
                    dbConfig.getUrl(), 
                    dbConfig.getUsername(), 
                    dbConfig.getPassword())) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库连接测试失败：" + e.getMessage());
        }
    }
    
    private ProductBatchConfig getProductBatchConfigEntity(String configId) {
        return productBatchConfigRepository.findById(configId)
            .orElseThrow(() -> new EntityNotFoundException("产品批次配置不存在：" + configId));
    }
    
    private ProductBatchConfigDTO convertToDTO(ProductBatchConfig config) {
        ProductBatchConfigDTO dto = new ProductBatchConfigDTO();
        dto.setConfigId(config.getConfigId());
        dto.setProductBatchId(config.getProductBatch().getProductBatchId());
        dto.setConfigName(config.getDbConfig().getConfigName());

        // 设置关联信息
        dto.setProductNameEn(config.getProductBatch().getProduct().getProductNameEn());
        dto.setProductNameCn(config.getProductBatch().getProduct().getProductNameCn());
        dto.setBatchNo(config.getProductBatch().getBatch().getBatchNo());
        dto.setDbType(config.getDbConfig().getType());
        dto.setDbUrl(config.getDbConfig().getUrl());
        dto.setDbUsername(config.getDbConfig().getUsername());
        dto.setDbSchema(config.getDbConfig().getSchema());
        
        dto.setCreatedAt(config.getCreatedAt());
        dto.setUpdatedAt(config.getUpdatedAt());
        dto.setLastModifiedBy(config.getLastModifiedBy());
        return dto;
    }
} 