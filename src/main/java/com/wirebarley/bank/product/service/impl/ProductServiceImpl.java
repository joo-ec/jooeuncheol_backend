package com.wirebarley.bank.product.service.impl;


import com.wirebarley.bank.common.entity.Product;
import com.wirebarley.bank.common.repository.ProductRepository;
import com.wirebarley.bank.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }
}
