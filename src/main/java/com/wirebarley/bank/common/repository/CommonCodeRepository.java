package com.wirebarley.bank.common.repository;

import com.wirebarley.bank.common.entity.Account;
import com.wirebarley.bank.common.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {

    CommonCode findByCodeAndParentCode(String code, String parentCode);
}
