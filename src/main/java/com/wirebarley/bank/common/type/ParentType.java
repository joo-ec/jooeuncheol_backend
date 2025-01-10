package com.wirebarley.bank.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParentType {
    BANK("CMMBAK"),         // 은행 코드
    PRODUCT("CMMPRD"),      // 상품 코드
    ROLES("CMMARZ"),        // 권한 코드
    MEMBER_TYPE("CMMUSR"),  // 회원 구분 코드
    USE("CMMUSE"),          // 사용 유무 코드
    APPROVAL("CMMAPR"),     // 승인 유무 코드
    JOIN("CMMJON"),         // 가입 구분 코드
    TRADE("CMMTRD");        // 거래 구분 코드

    private String code;
}
