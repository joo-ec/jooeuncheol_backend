package com.wirebarley.bank.common.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class TradeDetailsDTO {

    @Schema(description = "순번")
    private long NO;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "타 계좌 번호")
    private String targetAccountNumber;
    @Schema(description = "타 은행 코드")
    private String targetBankCode;
    @Schema(description = "거래 구분")
    private String tradeType;
    @Schema(description = "거래 결과")
    private String tradeResult;
    @Schema(description = "금액")
    private long amount;
    @Schema(description = "수수료")
    private long fee;
    @Schema(description = "잔고")
    private long balance;
    @Schema(description = "등록 일자")
    private LocalDateTime registrationDate;
    @Schema(description = "등록 자")
    private String registrationId;
}

