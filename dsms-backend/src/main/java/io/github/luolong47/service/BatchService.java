package io.github.luolong47.service;

import io.github.luolong47.dto.BatchDTO;
import io.github.luolong47.entity.Batch;
import io.github.luolong47.repository.BatchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    
    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }
    
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public BatchDTO getBatch(String batchNo) {
        Batch batch = batchRepository.findById(batchNo)
                .orElseThrow(() -> new RuntimeException("批次不存在"));
        return convertToDTO(batch);
    }
    
    @Transactional
    public BatchDTO createBatch(BatchDTO batchDTO) {
        Batch batch = new Batch();
        batch.setBatchNo(batchDTO.getBatchNo());
        batch.setStartDate(batchDTO.getStartDateTime());
        batch.setEndDate(batchDTO.getEndDateTime());
        return convertToDTO(batchRepository.save(batch));
    }
    
    @Transactional
    public BatchDTO updateBatch(String batchNo, BatchDTO batchDTO) {
        Batch batch = getBatchEntity(batchNo);
        batch.setStartDate(batchDTO.getStartDateTime());
        batch.setEndDate(batchDTO.getEndDateTime());
        return convertToDTO(batchRepository.save(batch));
    }
    
    @Transactional
    public void deleteBatch(String batchNo) {
        Batch batch = getBatchEntity(batchNo);
        batchRepository.delete(batch);
    }

    private Batch getBatchEntity(String batchNo) {
        return batchRepository.findById(batchNo)
                .orElseThrow(() -> new RuntimeException("批次不存在"));
    }

    private BatchDTO convertToDTO(Batch batch) {
        BatchDTO dto = new BatchDTO();
        dto.setBatchNo(batch.getBatchNo());
        dto.setStartDate(batch.getStartDate().toLocalDate());
        dto.setEndDate(batch.getEndDate().toLocalDate());
        dto.setCreatedAt(batch.getCreatedAt());
        return dto;
    }
} 