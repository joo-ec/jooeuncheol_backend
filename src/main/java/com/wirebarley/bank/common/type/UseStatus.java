package com.wirebarley.bank.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UseStatus {
    USE("CMMUSE001"),
    NOT_USED("CMMUSE002");

    private String code;
}
