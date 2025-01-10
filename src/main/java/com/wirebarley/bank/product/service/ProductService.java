package com.wirebarley.bank.product.service;

import com.wirebarley.bank.common.entity.Product;

public interface ProductService {

    Product findByProductCode(String productCode);
}
