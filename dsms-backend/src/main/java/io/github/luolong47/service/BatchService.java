package io.github.luolong47.service;

import io.github.luolong47.dto.BatchDTO;
import io.github.luolong47.entity.Batch;
import io.github.luolong47.repository.BatchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    
    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }
    
    public List<Batch> getAllBatches() {
        return batchRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public Batch getBatch(String batchNo) {
        return batchRepository.findById(batchNo)
                .orElseThrow(() -> new RuntimeException("批次不存在"));
    }
    
    @Transactional
    public Batch createBatch(BatchDTO batchDTO) {
        Batch batch = new Batch();
        batch.setBatchNo(batchDTO.getBatchNo());
        batch.setStartDate(batchDTO.getStartDateTime());
        batch.setEndDate(batchDTO.getEndDateTime());
        return batchRepository.save(batch);
    }
    
    @Transactional
    public Batch updateBatch(String batchNo, BatchDTO batchDTO) {
        Batch batch = getBatch(batchNo);
        batch.setStartDate(batchDTO.getStartDateTime());
        batch.setEndDate(batchDTO.getEndDateTime());
        return batchRepository.save(batch);
    }
    
    @Transactional
    public void deleteBatch(String batchNo) {
        Batch batch = getBatch(batchNo);
        batchRepository.delete(batch);
    }
} 