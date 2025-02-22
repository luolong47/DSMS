package io.github.luolong47.service;

import io.github.luolong47.dto.ProductDTO;
import io.github.luolong47.entity.Product;
import io.github.luolong47.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.domain.Sort;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    public Product getProduct(String productNameEn) {
        return productRepository.findById(productNameEn)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
    }
    
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return productRepository.save(product);
    }
    
    @Transactional
    public Product updateProduct(String productNameEn, ProductDTO productDTO) {
        Product product = getProduct(productNameEn);
        product.setProductNameCn(productDTO.getProductNameCn());
        product.setDescription(productDTO.getDescription());
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(String productNameEn) {
        Product product = getProduct(productNameEn);
        productRepository.delete(product);
    }
}