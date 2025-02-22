package io.github.luolong47.repository;

import io.github.luolong47.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, String>, JpaSpecificationExecutor<ProductBatch> {
    @Query("SELECT pb FROM ProductBatch pb JOIN FETCH pb.product JOIN FETCH pb.batch")
    List<ProductBatch> findAllWithProductAndBatch();
    
    List<ProductBatch> findByProduct_ProductNameEnContainingIgnoreCase(String productNameEn);
    List<ProductBatch> findByBatch_BatchNoContainingIgnoreCase(String batchNo);
} 