package com.wirebarley.bank.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200),            // Success
    SERVER_ERROR(9999),       // Server Error

    AUTH_ERROR(1000),        // Authentication failed
    NOT_FOUND_MEMBER(1001),  // Authentication failed
    INVALID_MEMBER(1002),  // Authentication failed

    ENCODING_ERR(2101),
    DECODING_ERR(2102),

    INVALID_BANK(3000),

    NOT_FOUND_ACCOUNT(3100),
    INVALID_ACCOUNT(3101),
    NO_ACCESS_ACCOUNT(3102),
    NOT_USED_ACCOUNT(3103),
    NOT_APPROVED_ACCOUNT(3104),
    ALREADY_APPROVAL_ACCOUNT(3105),

    INVALID_PRODUCT(3200),
    INVALID_AMOUNT_TRADE(3300),
    INVALID_WITHDRAWAL_AMOUNT_TRADE(3301),
    OVER_BOUNDS_TRADE(3302),

    NOT_FOUND(404);

    private int code;
}
