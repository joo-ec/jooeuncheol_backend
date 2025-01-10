package com.wirebarley.bank.common.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Schema(description = "거래 내역 조회 DTO")
@Data
@Builder
public class TransferDetailsRequest {
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "계좌 비밀번호")
    private String accountPassword;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "거래 유형")
    private String tradeType;
    @Schema(description = "거래 결과")
    private String tradeResult;
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "거래 시작일")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startTradeDT;
    @Schema(description = "거래 종료일")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endTradeDT;
}
