package com.wirebarley.bank.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeType {
    DEPOSIT("CMMTRD001"),
    WITHDRAWAL("CMMTRD002"),
    TRANSFER("CMMTRD003"),
    ANOTHER_TRANSFER("CMMTRD004");

    private String code;
}
