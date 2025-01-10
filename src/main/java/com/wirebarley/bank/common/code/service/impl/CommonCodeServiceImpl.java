package com.wirebarley.bank.common.code.service.impl;

import com.wirebarley.bank.common.code.service.CommonCodeService;
import com.wirebarley.bank.common.entity.CommonCode;
import com.wirebarley.bank.common.repository.CommonCodeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {
    private final CommonCodeRepository commonCodeRepository;

    @Override
    public CommonCode getCodeDetail(String code, String parentCode) {
        return commonCodeRepository.findByCodeAndParentCode(code, parentCode);
    }

}
