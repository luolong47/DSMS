package io.github.luolong47.service;

import io.github.luolong47.dto.ProductDTO;
import io.github.luolong47.entity.Product;
import io.github.luolong47.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ProductDTO getProduct(String productNameEn) {
        Product product = getProductEntity(productNameEn);
        return convertToDTO(product);
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return convertToDTO(productRepository.save(product));
    }
    
    @Transactional
    public ProductDTO updateProduct(String productNameEn, ProductDTO productDTO) {
        Product product = getProductEntity(productNameEn);
        product.setProductNameCn(productDTO.getProductNameCn());
        product.setDescription(productDTO.getDescription());
        return convertToDTO(productRepository.save(product));
    }
    
    @Transactional
    public void deleteProduct(String productNameEn) {
        Product product = getProductEntity(productNameEn);
        productRepository.delete(product);
    }

    private Product getProductEntity(String productNameEn) {
        return productRepository.findById(productNameEn)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}