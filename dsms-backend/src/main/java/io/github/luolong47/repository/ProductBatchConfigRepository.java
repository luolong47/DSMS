package io.github.luolong47.repository;

import io.github.luolong47.entity.ProductBatchConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchConfigRepository extends JpaRepository<ProductBatchConfig, String>, JpaSpecificationExecutor<ProductBatchConfig> {
    @Query("SELECT pbc FROM ProductBatchConfig pbc JOIN FETCH pbc.productBatch pb JOIN FETCH pb.product p JOIN FETCH pb.batch b JOIN FETCH pbc.dbConfig")
    List<ProductBatchConfig> findAllWithDetails();
    
    List<ProductBatchConfig> findByProductBatch_Product_ProductNameEnContainingIgnoreCase(String productNameEn);
    List<ProductBatchConfig> findByProductBatch_Batch_BatchNoContainingIgnoreCase(String batchNo);
    List<ProductBatchConfig> findByDbConfig_ConfigNameContainingIgnoreCase(String configName);
} 