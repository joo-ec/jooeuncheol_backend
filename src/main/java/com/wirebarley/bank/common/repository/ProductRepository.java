package com.wirebarley.bank.common.repository;

import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductCode(String productCode);

}
