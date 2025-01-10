package com.wirebarley.bank.common.code.service;

import com.wirebarley.bank.common.entity.CommonCode;

public interface CommonCodeService {

    CommonCode getCodeDetail(String code, String parentCode);
}
