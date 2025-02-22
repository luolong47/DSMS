package io.github.luolong47.service;

import io.github.luolong47.dto.ProductBatchDTO;
import io.github.luolong47.entity.Batch;
import io.github.luolong47.entity.Product;
import io.github.luolong47.entity.ProductBatch;
import io.github.luolong47.repository.BatchRepository;
import io.github.luolong47.repository.ProductBatchRepository;
import io.github.luolong47.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBatchService {
    private final ProductBatchRepository productBatchRepository;
    private final ProductRepository productRepository;
    private final BatchRepository batchRepository;
    
    public ProductBatchService(
            ProductBatchRepository productBatchRepository,
            ProductRepository productRepository,
            BatchRepository batchRepository) {
        this.productBatchRepository = productBatchRepository;
        this.productRepository = productRepository;
        this.batchRepository = batchRepository;
    }
    
    public List<ProductBatchDTO> getAllProductBatches() {
        return productBatchRepository.findAllWithProductAndBatch()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductBatchDTO> searchProductBatches(String productNameEn, String batchNo, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<ProductBatch> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Join<ProductBatch, Product> productJoin = root.join("product", JoinType.INNER);
            Join<ProductBatch, Batch> batchJoin = root.join("batch", JoinType.INNER);
            
            if (productNameEn != null && !productNameEn.isEmpty()) {
                predicates.add(cb.like(cb.lower(productJoin.get("productNameEn")), 
                    "%" + productNameEn.toLowerCase() + "%"));
            }
            
            if (batchNo != null && !batchNo.isEmpty()) {
                predicates.add(cb.like(cb.lower(batchJoin.get("batchNo")), 
                    "%" + batchNo.toLowerCase() + "%"));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(batchJoin.get("startDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(batchJoin.get("endDate"), endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return productBatchRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ProductBatchDTO getProductBatch(String productBatchId) {
        ProductBatch productBatch = getProductBatchEntity(productBatchId);
        return convertToDTO(productBatch);
    }
    
    @Transactional
    public ProductBatchDTO createProductBatch(ProductBatchDTO productBatchDTO) {
        Product product = productRepository.findById(productBatchDTO.getProductNameEn())
            .orElseThrow(() -> new RuntimeException("产品不存在"));
            
        Batch batch = batchRepository.findById(productBatchDTO.getBatchNo())
            .orElseThrow(() -> new RuntimeException("批次不存在"));
            
        // 检查是否已存在相同的关联
        String productBatchId = productBatchDTO.getProductNameEn() + "_" + productBatchDTO.getBatchNo();
        if (productBatchRepository.existsById(productBatchId)) {
            throw new RuntimeException("该产品批次关联已存在");
        }
            
        ProductBatch productBatch = new ProductBatch();
        productBatch.setProductBatchId(productBatchId);
        productBatch.setProduct(product);
        productBatch.setBatch(batch);
        
        return convertToDTO(productBatchRepository.save(productBatch));
    }
    
    @Transactional
    public void deleteProductBatch(String productBatchId) {
        ProductBatch productBatch = getProductBatchEntity(productBatchId);
        productBatchRepository.delete(productBatch);
    }
    
    private ProductBatch getProductBatchEntity(String productBatchId) {
        return productBatchRepository.findById(productBatchId)
            .orElseThrow(() -> new RuntimeException("产品批次关联不存在"));
    }
    
    private ProductBatchDTO convertToDTO(ProductBatch productBatch) {
        ProductBatchDTO dto = new ProductBatchDTO();
        dto.setProductBatchId(productBatch.getProductBatchId());
        dto.setProductNameEn(productBatch.getProduct().getProductNameEn());
        dto.setProductNameCn(productBatch.getProduct().getProductNameCn());
        dto.setBatchNo(productBatch.getBatch().getBatchNo());
        dto.setBatchStartDate(productBatch.getBatch().getStartDate());
        dto.setBatchEndDate(productBatch.getBatch().getEndDate());
        dto.setCreatedAt(productBatch.getCreatedAt());
        dto.setUpdatedAt(productBatch.getUpdatedAt());
        dto.setLastModifiedBy(productBatch.getLastModifiedBy());
        return dto;
    }
} 