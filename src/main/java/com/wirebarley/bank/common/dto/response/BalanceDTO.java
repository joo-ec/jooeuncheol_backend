package com.wirebarley.bank.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "계좌 잔고 조회 DTO")
@Data
@Builder
public class BalanceDTO {
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "은행 코드")
    private String bankCode;
    @Schema(description = "잔고")
    private long balance;
    @Schema(description = "수수료")
    private long fee;
    @Schema(description = "출금금액")
    private long amount;

}
