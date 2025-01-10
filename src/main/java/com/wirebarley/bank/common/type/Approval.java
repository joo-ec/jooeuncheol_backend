package com.wirebarley.bank.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Approval {
    APPROVAL("CMMAPR001"),
    NOT_APPROVED("CMMAPR002");

    private String code;
}
